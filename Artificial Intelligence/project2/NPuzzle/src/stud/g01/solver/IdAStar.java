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
        // 如果估价函数大于 depth（当前限制深度），返回失败，回溯
        if (node.getPathCost() >= depth) {
            fsmall = Integer.min(node.getPathCost(), fsmall);
            return false;
        }
        // 迭代增加 depth，直到找到解为止
        if (problem.goal(node.getState())) //找到了
        {
            path = generatePath(node);
            return true;
        }
        expanded.add(node.getState().hashCode());  // 将当前状态加入 expanded Set
        List<Node> childNodes = problem.childNodes(node, predictor);
        for (var child : childNodes){
            explored.add(child.getState().hashCode());
            // 遍历子节点，如果子节点的状态已经在explored集合中，或者子节点的状态与父节点的状态相同，就超越这个子节点1
            if (!(father == null || !child.getState().equals(father.getState())))
                continue;
            // 如果子状态的估价函数值小于当前限制深度，说明可能存在解，继续 DFS 搜索
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
//     * 构造函数，传入预测器 predictor
//     */
//    public IdAStar(Predictor predictor) {
//        this.predictor = predictor;
//    }
//
//    /**
//     * 提供一个接口来进行搜索过程
//     */
//    @Override
//    public Deque<Node> search(Problem problem) {
//        this.problem = problem;
//        if (!problem.solvable()) {
//            return null;
//        }
//        Node rootNode = problem.root(predictor);
//        // 初始的 depth 为根节点的估价函数值
//        int depth = rootNode.getHeuristic();
//        exploredSet.clear(); // 每次搜索前清空 explored 和 expanded Set
//        expandedSet.clear();
//        // 迭代增加 depth，直到找到解为止
//        while (!dfs(rootNode, null, depth)) {
//            depth++;
//        }
//        return path;
//    }
//
//    /**
//     * 获取已扩展（生成）的节点数量
//     */
//    @Override
//    public int nodesGenerated() {
//        return exploredSet.size();
//    }
//
//    /**
//     * 获取已扩展的节点数量
//     */
//    @Override
//    public int nodesExpanded() {
//        return expandedSet.size();
//    }
//
//    /**
//     * DFS 搜索过程
//     */
//    private boolean dfs(Node node, Node fatherNode, int depth) {
//        // 如果估价函数大于 depth（当前限制深度），返回失败，回溯
//        if (node.getPathCost() >= depth) {
//            return false;
//        }
//        // 如果搜索到目标状态，记录路径，并返回成功
//        if (problem.goal(node.getState())) {
//            path = generatePath(node);
//            return true;
//        }
//        expandedSet.add(node.getState().hashCode()); // 将当前状态加入 expanded Set
//        // 生成子状态，并按照估价函数排序
//        List<Node> childNodes = problem.childNodes(node, predictor);
//        for (var childNode : childNodes) {
//            // 生成状态和父状态相等并且深度大于 2 的节点只会在交错过程中被生成，跳过扩展
//            if (!(fatherNode == null || !childNode.getState().equals(fatherNode.getState()))) {
//                continue;
//            }
//            // 生成新的子状态，加入 explored Set
//            exploredSet.add(childNode.getState().hashCode());
//            // 如果子状态的估价函数值小于当前限制深度，说明可能存在解，继续 DFS 搜索
//            if (childNode.getHeuristic() < depth && dfs(childNode, node, depth)) {
//                return true; // 如果找到了解，返回成功
//            }
//        }
//        return false; // 如果子节点中没有找到解，回溯
//    }
//
//}