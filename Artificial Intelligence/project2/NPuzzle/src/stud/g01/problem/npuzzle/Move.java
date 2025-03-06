package stud.g01.problem.npuzzle;

import core.problem.Action;
import stud.g01.problem.npuzzle.Direction;

/**
 *
 * Ѱ·���ƶ��������������������NSEW�ĸ����򣬻���8������
 */
public class Move extends Action {

    private final Direction direction;

    public Move(Direction direction) {
        this.direction = direction;
    }

    public stud.g01.problem.npuzzle.Direction getDirection() {
        return direction;
    }

    @Override
    public void draw() {
        System.out.println(this);
    }

    @Override
    public int stepCost() {
        return 1;
    }

    @Override
    public String toString() {
        return direction.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Move) {
            Move another = (Move) obj;
            //����Node�����״̬��ͬ������Ϊ����ͬ��
            return this.direction.equals(another.direction);
        }
        return false;
    }
}
