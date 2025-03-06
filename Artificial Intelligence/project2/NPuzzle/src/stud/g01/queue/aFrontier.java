package stud.g01.queue;
import core.problem.State;
import core.solver.queue.Node;
import core.solver.queue.Frontier;

import java.util.*;

public class aFrontier extends PriorityQueue<Node> implements Frontier {

    // 节点优先级比较器，在Node类中定义了三个不同的比较器（
    //      Dijkstra,
    //      Greedy Best-First,
    //      Best-First
    // evaluator决定了优先取出Frontier中的哪个元素。
    private final Comparator<Node> evaluator;

    public aFrontier(Comparator<Node> evaluator) {
        // this(DEFAULT_INITIAL_CAPACITY, comparator) 源码中可以传入一个比较器 这里用节点比较
        // DEFAULT_INITIAL_CAPACITY默认容量为15
        super(evaluator);
        this.evaluator = evaluator;
    }

    /* 由于aFrontier继承了 PriorityQueue 且 Node类实现了Comparable接口 所以可以用优先队列存放节点
    并且比较节点的大小(f = g + h)
    1.首先实现getNode 通过状态在frontier中查询节点并返回
    在框架给出的frontier中继承了arraylist 把节点存在其中
    在getNode实现时是遍历了容器 通过比较重写的 equals来判断
    这样的做法会导致速度下降 时间复杂度起码O(n)
    这里我们如果用映射 也就是使用hash表 当我们入队一个 就在哈希表中put一个
    这样如果我们要查询某个节点 就不用遍历队列了 直接通过hash表查找就可以了O(1)
    hashmap的泛型是int, Node 我们key不用Node是因为当我们对象改变某个值的时候会导致hashcode变化 所以就会找不到值
    我们可以采用key直接存放格局的hash来解决

     */
    private final HashMap<Integer, Node> hm = new HashMap<>();  // close表
    private Node getNode(State state) {
        return hm.get(state.hashCode());
    }
    // 从队列中取出第一个节点 也就是f最小的节点 并从队列中删除
    public Node poll() {
        Node smallest = super.poll();
        // 在hashmap中也删除
        hm.remove(smallest.getState().hashCode());
        return smallest;
    }
    @Override
    public boolean contains(Node node) {
        return getNode(node.getState()) != null;
    }

    /**
     * 将结点node插入到优先队列中，
     * 如果Frontier中已经存在与node状态相同的结点，则舍弃掉二者中估值更大的结点
     * @param node 要插入优先队列的结点
     * @return
     */
    @Override
    public boolean offer(Node node) throws NullPointerException {
        Node n1 = getNode(node.getState());  // 查看当前队列里面是否已经有这个节点了
        if (n1 == null) {  // 如果队列里面没有
            super.offer(node); // 就放入优先队列
            hm.put(node.getState().hashCode(), node);  // 存入hashmap中
            return true;
        }
        // 如果队列里面已经存在此节点 那么就保留f值小的
        replace(n1, node);
        return false;
    }


    /**
     * 用节点 e 替换掉具有相同状态的旧节点 oldNode 同学们可能需要改写这个函数！
     *
     * @param oldNode 被替换的结点
     * @param newNode 新结点
     */
    private void replace(Node oldNode, Node newNode) {
        int c = evaluator.compare(oldNode, newNode);  // 比较两个节点 大于0就舍弃旧的节点
        if (c > 0) {  // 大于0代表 旧节点的f > 新节点的f
            super.offer(newNode);
            // 更新hashmap
            hm.put(newNode.getState().hashCode(), newNode);
        }
    }
}

