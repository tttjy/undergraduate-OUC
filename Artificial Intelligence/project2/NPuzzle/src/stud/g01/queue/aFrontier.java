package stud.g01.queue;
import core.problem.State;
import core.solver.queue.Node;
import core.solver.queue.Frontier;

import java.util.*;

public class aFrontier extends PriorityQueue<Node> implements Frontier {

    // �ڵ����ȼ��Ƚ�������Node���ж�����������ͬ�ıȽ�����
    //      Dijkstra,
    //      Greedy Best-First,
    //      Best-First
    // evaluator����������ȡ��Frontier�е��ĸ�Ԫ�ء�
    private final Comparator<Node> evaluator;

    public aFrontier(Comparator<Node> evaluator) {
        // this(DEFAULT_INITIAL_CAPACITY, comparator) Դ���п��Դ���һ���Ƚ��� �����ýڵ�Ƚ�
        // DEFAULT_INITIAL_CAPACITYĬ������Ϊ15
        super(evaluator);
        this.evaluator = evaluator;
    }

    /* ����aFrontier�̳��� PriorityQueue �� Node��ʵ����Comparable�ӿ� ���Կ��������ȶ��д�Žڵ�
    ���ұȽϽڵ�Ĵ�С(f = g + h)
    1.����ʵ��getNode ͨ��״̬��frontier�в�ѯ�ڵ㲢����
    �ڿ�ܸ�����frontier�м̳���arraylist �ѽڵ��������
    ��getNodeʵ��ʱ�Ǳ��������� ͨ���Ƚ���д�� equals���ж�
    �����������ᵼ���ٶ��½� ʱ�临�Ӷ�����O(n)
    �������������ӳ�� Ҳ����ʹ��hash�� ���������һ�� ���ڹ�ϣ����putһ��
    �����������Ҫ��ѯĳ���ڵ� �Ͳ��ñ��������� ֱ��ͨ��hash����ҾͿ�����O(1)
    hashmap�ķ�����int, Node ����key����Node����Ϊ�����Ƕ���ı�ĳ��ֵ��ʱ��ᵼ��hashcode�仯 ���Ծͻ��Ҳ���ֵ
    ���ǿ��Բ���keyֱ�Ӵ�Ÿ�ֵ�hash�����

     */
    private final HashMap<Integer, Node> hm = new HashMap<>();  // close��
    private Node getNode(State state) {
        return hm.get(state.hashCode());
    }
    // �Ӷ�����ȡ����һ���ڵ� Ҳ����f��С�Ľڵ� ���Ӷ�����ɾ��
    public Node poll() {
        Node smallest = super.poll();
        // ��hashmap��Ҳɾ��
        hm.remove(smallest.getState().hashCode());
        return smallest;
    }
    @Override
    public boolean contains(Node node) {
        return getNode(node.getState()) != null;
    }

    /**
     * �����node���뵽���ȶ����У�
     * ���Frontier���Ѿ�������node״̬��ͬ�Ľ�㣬�������������й�ֵ����Ľ��
     * @param node Ҫ�������ȶ��еĽ��
     * @return
     */
    @Override
    public boolean offer(Node node) throws NullPointerException {
        Node n1 = getNode(node.getState());  // �鿴��ǰ���������Ƿ��Ѿ�������ڵ���
        if (n1 == null) {  // �����������û��
            super.offer(node); // �ͷ������ȶ���
            hm.put(node.getState().hashCode(), node);  // ����hashmap��
            return true;
        }
        // ������������Ѿ����ڴ˽ڵ� ��ô�ͱ���fֵС��
        replace(n1, node);
        return false;
    }


    /**
     * �ýڵ� e �滻��������ͬ״̬�ľɽڵ� oldNode ͬѧ�ǿ�����Ҫ��д���������
     *
     * @param oldNode ���滻�Ľ��
     * @param newNode �½��
     */
    private void replace(Node oldNode, Node newNode) {
        int c = evaluator.compare(oldNode, newNode);  // �Ƚ������ڵ� ����0�������ɵĽڵ�
        if (c > 0) {  // ����0���� �ɽڵ��f > �½ڵ��f
            super.offer(newNode);
            // ����hashmap
            hm.put(newNode.getState().hashCode(), newNode);
        }
    }
}

