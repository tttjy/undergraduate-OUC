package stud.g01.DB;

import java.util.ArrayList;

public class State {
    // �洢һ�� block ״̬
    private int state;
    // һ�� block �Ļ������������ָ������һ�� block
    private int num;

    // ���캯��
    public State() {
        this.state = 0;
    }

    public State(int state, int num) {
        this.state = state;
        this.num = num;
    }

    public State(State a) {
        this.state = a.state;
        this.num = a.num;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getState() {
        return this.state;
    }

    public int getNum() {
        return this.num;
    }

    // ��һ��slides���й����һ��State��ÿ��byte��block�л���λ����Ϣ�����1byte��ո�λ����Ϣ
    public static State slidesToState(ArrayList<Integer> slides) {
        int res = 0; // res�����1byteΪ�ո�
        for (int i = 0; i < 4 * slides.size(); i++) { // i��res�Ķ������±�
            int j = slides.size() - i / 4 - 1; // j��slides���±�
            if((((slides.get(j) - 1) >> (i % 4)) & 1) == 1) { // ��slides[j]�浽res��һ��byte��
                res |= 1 << i; // ����iλ��Ϊ1
            }
        }

        return new State(res, slides.size()); // ����һ��State
    }

    // ��ȡstate��indexλ���ϵĻ�����Ϣ
    public int stateToSlide(int index) {
        int res = this.state >> ((this.num - index - 1) * 4) & 15;
        return res + 1;
    }

    // ��state���޸�indexλ���ϻ����λ����ϢΪvalue
    public void setStateSlide(int index, int value) {
        value--;
        int base = this.num - index - 1;
        for (int i = 0; i < 4; i++) {
            if (((value >> i) & 1) == 1) {
                this.state |= (1 << (base * 4 + i)); // �ĳ�1
            } else {
                this.state &= ~(1 << (base * 4 + i)); // �ĳ�0
            }
        }
    }

    public void slidePrint() {
        for(int i = 0; i < this.num; i++) {
            System.out.print(this.stateToSlide(i) + " ");
        }
        System.out.println();
    }
}
