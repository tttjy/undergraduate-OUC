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
 * ��ѧ���������㷨���м���������
 * arg0: ������������      resources/pathfinding.txt
 * arg1: ��������         PATHFINDING
 * arg2: ��Ŀ���ĸ��׶�    1
 * arg3: ��С���Feeder   stud.runner.WalkerFeeder
 */
public final class SearchTester {
    public SearchTester() throws IOException {
    }

    //ͬѧ�ǿ��Ը����Լ�����Ҫ�������޸ġ�
    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, InterruptedException {

        //����args[3]�ṩ����������ѧ����EngineFeeder����
        EngineFeeder feeder = (EngineFeeder)
                Class.forName(args[3])
                        .getDeclaredConstructor().newInstance();

        ////���ļ��������������������ı��� args[0]�����������ļ������·��
        Scanner scanner = new Scanner(new File(args[0]));
        ArrayList<String> problemLines = getProblemLines(scanner);

        //feeder�����������ı���ȡѰ·���������ʵ��
        ArrayList<Problem> problems = feeder.getProblems(problemLines);
        ////����ʵ�����뵽ArrayList��

        //��ǰ��������� args[1]    Ѱ·���⣬�������̣�Ұ�˴���ʿ���ӵ�
        ProblemType type = ProblemType.valueOf(args[1]);
        //����ڼ��׶� args[2]
        int step = Integer.parseInt(args[2]);

        //�����������ͺ͵�ǰ�׶Σ���ȡ������������������
        //Ѱ·����ֱ�ʹ��Grid�����Euclid������Ϊ��������
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
            //solveProblems�������ݲ�ͬ�����������ɲ�ͬ��searcher
            //��Feeder��ȡ��ʹ�õ��������棨AStar��IDAStar�ȣ���     

            System.out.println();
        }
    }

    /**
     * �����������ͺ͵�ǰ�׶Σ���ȡ������������������
     *
     * @param type
     * @param step
     * @return
     */
    private static ArrayList<HeuristicType> getHeuristicTypes(ProblemType type, int step) {
        //��⵱ǰ�����ڵ�ǰ�׶ο��õ��������������б�
        ArrayList<HeuristicType> heuristics = new ArrayList<>();
        //���ݲ�ͬ���������ͣ����в�ͬ�Ĳ���
        if (type == ProblemType.PATHFINDING) {
            heuristics.add(PF_GRID);
            heuristics.add(PF_EUCLID);
        } else {
            //NPuzzle����ĵ�һ�׶Σ�ʹ�ò���λ���ƺ������پ���
            if (step == 1 || step == 2) {
                heuristics.add(LINEAR_CONFLICT);
                heuristics.add(MANHATTAN);
//                heuristics.add(MISPLACED);

//                PatternDataBase db = new PatternDataBase();
//                heuristics.add(DISJOINT_PATTERN);
            }
            //NPuzzle����ĵ����׶Σ�ʹ��Disjoint Pattern
            else if (step == 3) {
                PatternDataBase db = new PatternDataBase();
                heuristics.add(DISJOINT_PATTERN);
                heuristics.add(LINEAR_CONFLICT);
            }
        }
        return heuristics;
    }

    /**
     * ʹ�ø�����searcher��������⼯���е��������⣬ͬʱʹ�ý���������õĽ���м��
     *
     * @param problems     ���⼯��
     * @param searcher     searcher
     * @param heuristicType ʹ����������������
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
            // ʹ��AStar�����������
            Zobrist.getZobrist(3);
            StopwatchCPU timer1 = new StopwatchCPU();
            Deque<Node> path = searcher.search(problem);
            double time1 = timer1.elapsedTime();

            if (path == null) {
                System.out.println("No Solution" + "��ִ����" + time1 + "s��" +
                        "��������" + searcher.nodesGenerated() + "����㣬" +
                        "��չ��" + searcher.nodesExpanded() + "�����");
                continue;
            }

            if (flag == 0)
                problem.savePathToCustomFile("C:\\Users\\len\\Desktop\\my1\\n-puzzle\\resources\\data.txt", path, problem, problems.size());

            System.out.println("��������Ϊ��" + heuristicType + "��·�����ȣ�" + path.size() + "����ʱ" + time1 + "s��" +
                    "������" + searcher.nodesGenerated() + "����㣬" +
                    "��չ��" + searcher.nodesExpanded() + "�����");
//            String l = time1 + " " + searcher.nodesGenerated() + " " + searcher.nodesExpanded();
//            writer.write(l);
//            writer.newLine();
        }
//        writer.close();
        ++flag;
    }

    /**
     * ���ļ���������ʵ�����ַ����������ַ���������
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