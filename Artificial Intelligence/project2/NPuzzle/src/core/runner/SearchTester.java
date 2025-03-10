package core.runner;

import algs4.util.StopwatchCPU;
import core.problem.Problem;
import core.problem.ProblemType;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.searcher.AbstractSearcher;
import core.solver.queue.Node;
import stud.g01.DB.PatternDataBase;
import stud.g01.problem.npuzzle.NPuzzleProblem;
import stud.g01.problem.npuzzle.Zobrist;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import static core.solver.algorithm.heuristic.HeuristicType.*;

/**
 * 对学生的搜索算法进行检测的主程序
 * arg0: 问题输入样例      resources/pathfinding.txt
 * arg1: 问题类型         PATHFINDING
 * arg2: 项目的哪个阶段    1
 * arg3: 各小组的Feeder   stud.runner.WalkerFeeder
 */
public final class SearchTester {
    public SearchTester() throws IOException {
    }

    //同学们可以根据自己的需要，随意修改。
    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, InterruptedException {

        //根据args[3]提供的类名生成学生的EngineFeeder对象
        EngineFeeder feeder = (EngineFeeder)
                Class.forName(args[3])
                        .getDeclaredConstructor().newInstance();

        ////从文件读入所有输入样例的文本； args[0]：输入样例文件的相对路径
        Scanner scanner = new Scanner(new File(args[0]));
        ArrayList<String> problemLines = getProblemLines(scanner);

        //feeder从输入样例文本获取寻路问题的所有实例
        ArrayList<Problem> problems = feeder.getProblems(problemLines);
        ////问题实例读入到ArrayList中

        //当前问题的类型 args[1]    寻路问题，数字推盘，野人传教士过河等
        ProblemType type = ProblemType.valueOf(args[1]);
        //任务第几阶段 args[2]
        int step = Integer.parseInt(args[2]);

        //根据问题类型和当前阶段，获取所有启发函数的类型
        //寻路问题分别使用Grid距离和Euclid距离作为启发函数
        ArrayList<HeuristicType> heuristics = getHeuristicTypes(type, step);

        for (HeuristicType heuristicType : heuristics) {
            if (step == 1) {
                solveProblems(problems, feeder.getAStar(heuristicType), heuristicType);
            }
            if (step == 2) {
                solveProblems(problems, feeder.getIdaStar(heuristicType), heuristicType);
            }
            if (step == 3) {
                PatternDataBase dataBaseCreate;
                solveProblems(problems, feeder.getIdaStar(heuristicType), heuristicType);
//               solveProblems(problems, feeder.getAStar(heuristicType), heuristicType,step);
            }
            //solveProblems方法根据不同启发函数生成不同的searcher
            //从Feeder获取所使用的搜索引擎（AStar，IDAStar等），     

            System.out.println();
        }
    }

    /**
     * 根据问题类型和当前阶段，获取所有启发函数的类型
     *
     * @param type
     * @param step
     * @return
     */
    private static ArrayList<HeuristicType> getHeuristicTypes(ProblemType type, int step) {
        //求解当前问题在当前阶段可用的启发函数类型列表
        ArrayList<HeuristicType> heuristics = new ArrayList<>();
        //根据不同的问题类型，进行不同的测试
        if (type == ProblemType.PATHFINDING) {
            heuristics.add(PF_GRID);
            heuristics.add(PF_EUCLID);
        } else {
            //NPuzzle问题的第一阶段，使用不在位将牌和曼哈顿距离
            if (step == 1 || step == 2) {
                heuristics.add(LINEAR_CONFLICT);
                heuristics.add(MANHATTAN);
//                heuristics.add(MISPLACED);

//                PatternDataBase db = new PatternDataBase();
//                heuristics.add(DISJOINT_PATTERN);
            }
            //NPuzzle问题的第三阶段，使用Disjoint Pattern
            else if (step == 3) {
                PatternDataBase db = new PatternDataBase();
                heuristics.add(DISJOINT_PATTERN);
                heuristics.add(LINEAR_CONFLICT);
            }
        }
        return heuristics;
    }

    /**
     * 使用给定的searcher，求解问题集合中的所有问题，同时使用解检测器对求得的解进行检测
     *
     * @param problems     问题集合
     * @param searcher     searcher
     * @param heuristicType 使用哪种启发函数？
     */

    static int flag = 0;

//    static File file = new File("resources/xxx.txt");
//    static BufferedWriter writer;
//
//    static {
//        try {
//            writer = new BufferedWriter(new FileWriter(file));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void solveProblems(ArrayList<Problem> problems, Searcher searcher, HeuristicType heuristicType) throws FileNotFoundException, IOException {
        if (flag == 0) {
            PrintWriter temp = new PrintWriter("resources/data.txt");
            temp.println(problems.size());
            temp.flush();
            temp.close();
        }
        for (Problem problem : problems) {
            // 使用AStar引擎求解问题
            Zobrist.getZobrist(3);
            StopwatchCPU timer1 = new StopwatchCPU();
            Deque<Node> path = searcher.search(problem);
            double time1 = timer1.elapsedTime();

            if (path == null) {
                System.out.println("No Solution" + "，执行了" + time1 + "s，" +
                        "共生成了" + searcher.nodesGenerated() + "个结点，" +
                        "扩展了" + searcher.nodesExpanded() + "个结点");
                continue;
            }

            if (flag == 0)
                problem.savePathToCustomFile("C:\\Users\\len\\Desktop\\my1\\n-puzzle\\resources\\data.txt", path, problem, problems.size());

            System.out.println("启发函数为：" + heuristicType + "，路径长度：" + path.size() + "，用时" + time1 + "s，" +
                    "生成了" + searcher.nodesGenerated() + "个结点，" +
                    "扩展了" + searcher.nodesExpanded() + "个结点");
//            String l = time1 + " " + searcher.nodesGenerated() + " " + searcher.nodesExpanded();
//            writer.write(l);
//            writer.newLine();
        }
//        writer.close();
        ++flag;
    }

    /**
     * 从文件读入问题实例的字符串，放入字符串数组里
     *
     * @param scanner
     * @return
     */
    public static ArrayList<String> getProblemLines(Scanner scanner) {
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }

}