package stud.g01.solver;

import core.problem.Problem;
import core.solver.algorithm.Searcher;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.algorithm.searcher.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.*;

public class IdAStar implements Searcher {
    Predictor predictor;
    private final Set<Integer> explored = new HashSet<>();
    private final Set<Integer> expanded = new HashSet<>();
    Problem problem;
    Deque<Node> path = new ArrayDeque<>();

    public int fsmall = Integer.MAX_VALUE;

    public IdAStar(Predictor predictor) {
        this.predictor = predictor;
    }
    @Override
    public Deque<Node> search(Problem p) {
        problem = p;
        if (!problem.solvable())
            return null;
        Node root = problem.root(predictor);
        int depth = root.getHeuristic();
        explored.clear();
        expanded.clear();
        while (!dfs(root, null, depth))
        {
            depth = depth + 1;
            fsmall = Integer.MAX_VALUE;
        }
        return path;
    }

    @Override
    public int nodesExpanded() {
        return expanded.size();
    }

    @Override
    public int nodesGenerated() {
        return explored.size();
    }

    public boolean dfs(Node node,Node father,int  depth){
        // ������ۺ������� depth����ǰ������ȣ�������ʧ�ܣ�����
        if (node.getPathCost() >= depth) {
            fsmall = Integer.min(node.getPathCost(), fsmall);
            return false;
        }
        // �������� depth��ֱ���ҵ���Ϊֹ
        if (problem.goal(node.getState())) //�ҵ���
        {
            path = generatePath(node);
            return true;
        }
        expanded.add(node.getState().hashCode());  // ����ǰ״̬���� expanded Set
        List<Node> childNodes = problem.childNodes(node, predictor);
        for (var child : childNodes){
            explored.add(child.getState().hashCode());
            // �����ӽڵ㣬����ӽڵ��״̬�Ѿ���explored�����У������ӽڵ��״̬�븸�ڵ��״̬��ͬ���ͳ�Խ����ӽڵ�1
            if (!(father == null || !child.getState().equals(father.getState())))
                continue;
            // �����״̬�Ĺ��ۺ���ֵС�ڵ�ǰ������ȣ�˵�����ܴ��ڽ⣬���� DFS ����
            if (child.evaluation() < depth && dfs(child, node, depth))
                return true;
        }
        return false;
    }

}
//package stud.g01.solver;
//
//import core.problem.Problem;
//import core.solver.algorithm.Searcher;
//import core.solver.algorithm.heuristic.Predictor;
//import core.solver.algorithm.searcher.AbstractSearcher;
//import core.solver.queue.Frontier;
//import core.solver.queue.Node;
//
//import java.util.*;
//
//public class IdAStar implements Searcher {
//    private Predictor predictor;
//    private Set<Integer> exploredSet = new HashSet<>();
//    private Set<Integer> expandedSet = new HashSet<>();
//    private Problem problem;
//    private Deque<Node> path = new ArrayDeque<>();
//
//    /**
//     * ���캯��������Ԥ���� predictor
//     */
//    public IdAStar(Predictor predictor) {
//        this.predictor = predictor;
//    }
//
//    /**
//     * �ṩһ���ӿ���������������
//     */
//    @Override
//    public Deque<Node> search(Problem problem) {
//        this.problem = problem;
//        if (!problem.solvable()) {
//            return null;
//        }
//        Node rootNode = problem.root(predictor);
//        // ��ʼ�� depth Ϊ���ڵ�Ĺ��ۺ���ֵ
//        int depth = rootNode.getHeuristic();
//        exploredSet.clear(); // ÿ������ǰ��� explored �� expanded Set
//        expandedSet.clear();
//        // �������� depth��ֱ���ҵ���Ϊֹ
//        while (!dfs(rootNode, null, depth)) {
//            depth++;
//        }
//        return path;
//    }
//
//    /**
//     * ��ȡ����չ�����ɣ��Ľڵ�����
//     */
//    @Override
//    public int nodesGenerated() {
//        return exploredSet.size();
//    }
//
//    /**
//     * ��ȡ����չ�Ľڵ�����
//     */
//    @Override
//    public int nodesExpanded() {
//        return expandedSet.size();
//    }
//
//    /**
//     * DFS ��������
//     */
//    private boolean dfs(Node node, Node fatherNode, int depth) {
//        // ������ۺ������� depth����ǰ������ȣ�������ʧ�ܣ�����
//        if (node.getPathCost() >= depth) {
//            return false;
//        }
//        // ���������Ŀ��״̬����¼·���������سɹ�
//        if (problem.goal(node.getState())) {
//            path = generatePath(node);
//            return true;
//        }
//        expandedSet.add(node.getState().hashCode()); // ����ǰ״̬���� expanded Set
//        // ������״̬�������չ��ۺ�������
//        List<Node> childNodes = problem.childNodes(node, predictor);
//        for (var childNode : childNodes) {
//            // ����״̬�͸�״̬��Ȳ�����ȴ��� 2 �Ľڵ�ֻ���ڽ�������б����ɣ�������չ
//            if (!(fatherNode == null || !childNode.getState().equals(fatherNode.getState()))) {
//                continue;
//            }
//            // �����µ���״̬������ explored Set
//            exploredSet.add(childNode.getState().hashCode());
//            // �����״̬�Ĺ��ۺ���ֵС�ڵ�ǰ������ȣ�˵�����ܴ��ڽ⣬���� DFS ����
//            if (childNode.getHeuristic() < depth && dfs(childNode, node, depth)) {
//                return true; // ����ҵ��˽⣬���سɹ�
//            }
//        }
//        return false; // ����ӽڵ���û���ҵ��⣬����
//    }
//
//}