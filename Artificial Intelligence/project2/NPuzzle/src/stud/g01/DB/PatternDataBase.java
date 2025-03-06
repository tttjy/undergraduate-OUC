package stud.g01.DB;

import java.io.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PatternDataBase {
    public final static int[] dx = {0, 1, 0, -1};
    public final static int[] dy = {1, 0, -1, 0};

    // 问题规模
    public final static int Size = 4;

    // 状态的花费，单表查询
    public static TreeMap<Integer, Integer> stateCost;

    // 从数据库加载数据
    public static ArrayList<ArrayList<Integer>> Table = new ArrayList<>() {
        {
            add(PatternDataBase.readFromFile("resources/663_0.txt"));
            add(PatternDataBase.readFromFile("resources/663_1.txt"));
            add(PatternDataBase.readFromFile("resources/663_2.txt"));
//            System.out.println("DataBase has been read successfully!");
        }
    };

    // 从数据库中读取cost
    public static ArrayList<Integer> readFromFile(String filePath) {
        Scanner fileIn;
        try {
            fileIn = new Scanner(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int state, cost;

        ArrayList<Integer> tmp = new ArrayList<>();

        while (fileIn.hasNext()) {
            state = fileIn.nextInt();
            cost = fileIn.nextInt();
            while (tmp.size() < state) {
                tmp.add(0);
            }
            tmp.add(cost);
        }
        return tmp;
    }

    // 启发函数，从 NPuzzle 格局获取距离
    public static int getDistance(byte[][] state, ArrayList<Block> blocks) {
        int distance = 0, index, value, blockIndex;
        ArrayList<State> states = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            states.add(State.slidesToState(blocks.get(i).getSlides()));
        }
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                if (state[i][j] == 0)
                    continue;
                blockIndex = Block.belongs.get(state[i][j]);
                index = blocks.get(blockIndex).getIndex(state[i][j]);
                value = i * Size + j + 1;
                states.get(blockIndex).setStateSlide(index, value);
            }
        }
        for (int i = 0; i < blocks.size(); i++) {
            distance += Table.get(i).get(states.get(i).getState());
        }
        return distance;
    }

    // 移动一次空格
    private static int makeMove(State state, int dx, int dy) {
        // 当前空格Index
        int blankIndex = state.stateToSlide(state.getNum() - 1);
        // 当前空格位置
        int blankX = (blankIndex - 1) / Size;
        int blankY = (blankIndex - 1) % Size;
        // 下一个空格位置
        int newX = blankX + dx;
        int newY = blankY + dy;

        if (newX < 0 || newX >= Size || newY < 0 || newY >= Size) {
            return -1;
        }
        // 下一个空格Index
        int newIndex = newX * Size + newY + 1;
        State newState = new State(state);

        // 记录cost是否增加
        int costIncrease = 0;
        // 判断原block中的滑块是否和空格发生交换
        for (int i = 0; i < state.getNum(); i++) {
            int x = (state.stateToSlide(i) - 1) / Size;
            int y = (state.stateToSlide(i) - 1) % Size;

            // 目标滑块为block中的滑块，需要修改状态中的滑块
            if (x == newX && y == newY) {
                newState.setStateSlide(i, blankIndex);
                costIncrease = 1;
                break;
            }
        }
        newState.setStateSlide(state.getNum() - 1, newIndex);

        // 如果已经访问过则直接返回-1
        if (stateCost.containsKey(newState.getState())) {
            return -1;
        }

        // 计算新的cost
        int newCost = stateCost.get(state.getState()) + costIncrease;
        stateCost.put(newState.getState(), newCost);

        // 更新当前状态
        state.setState(newState.getState());

        return costIncrease;
    }

    // 01BFS 从目标状态搜到当前状态，可计算估价函数
    private static void BreadthFirstSearch(Block block) {
        stateCost = new TreeMap<>();
        // 双端队列
        Deque<Integer> deq = new LinkedList<>();
        ArrayList<Integer> slides = block.getSlides();

        // 在slides最后存空格
        slides.add(Size * Size);
        State tmp = State.slidesToState(slides);

        stateCost.put(tmp.getState(), 0);
        deq.addLast(tmp.getState());

        int stateNow, exitFlag, epoch = 0;

        while(!deq.isEmpty()) {
            epoch++;
            if (epoch % 1000000 == 0) {
                System.out.println("epoch:" + epoch);
            }

            stateNow = deq.getFirst();
            deq.removeFirst();
            for (int i = 0; i < 4; i++) {
                tmp.setState(stateNow);
                exitFlag = makeMove(tmp, dx[i], dy[i]);

                if (exitFlag == 0) {
                    deq.addFirst(tmp.getState());
                } else if (exitFlag == 1) {
                    deq.addLast(tmp.getState());
                }
            }
        }
        System.out.println("finished all epoch is " + epoch);
    }

    // 将所有状态写入文件中
    private static void writeToFile(String filePreName, int index) {
        TreeMap<Integer, Integer> tmp = new TreeMap<>();
        String path = "./" + filePreName + "_" + index + ".txt";
        File file = new File(path);
        FileOutputStream fileOut;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOut = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int state, cost;
        Iterator iter = PatternDataBase.stateCost.entrySet().iterator();
        Map.Entry entry;

        // 去重
        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            state = ((int) entry.getKey()) >> 4;
            cost = (int) entry.getValue();
            if (tmp.containsKey(state)) {
                tmp.put(state, Math.min(cost, tmp.get(state)));
            } else {
                tmp.put(state, cost);
            }
        }

        Iterator it = tmp.entrySet().iterator();
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            state = (int) entry.getKey();
            cost = (int) entry.getValue();
            try {
                String text = state + " " +cost + "\n";
                fileOut.write(text.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // stateCost映射清空
        stateCost.clear();
        try {
            fileOut.close();
            System.out.println("file closed successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 根据 blocks 可变参数创建模式数据库
    public static void create(String filePreName, Block ... blocks) {
        int i = 0;
        for (Block b: blocks) {
            System.out.println("this is the " + i + " block");
            BreadthFirstSearch(b);
            PatternDataBase.writeToFile(filePreName, i++);
        }
    }

    // 根据 blocks 数组创建模式数据库
    public static void create(String filePreName, ArrayList<Block> blocks) {
        int i = 0;
        // 对每一个块进行搜索
        for (Block b: blocks) {
            System.out.println("this is the " + i + " block");
            BreadthFirstSearch(b);
            PatternDataBase.writeToFile(filePreName, i++);
        }
    }
}

