package stud.g01.DB;

import java.util.ArrayList;

public class State {
    // 存储一个 block 状态
    private int state;
    // 一个 block 的滑块个数，用于指明是哪一个 block
    private int num;

    // 构造函数
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

    // 将一个slides序列构造成一个State，每个byte存block中滑块位置信息，最后1byte存空格位置信息
    public static State slidesToState(ArrayList<Integer> slides) {
        int res = 0; // res的最后1byte为空格
        for (int i = 0; i < 4 * slides.size(); i++) { // i是res的二进制下标
            int j = slides.size() - i / 4 - 1; // j是slides的下标
            if((((slides.get(j) - 1) >> (i % 4)) & 1) == 1) { // 将slides[j]存到res的一个byte中
                res |= 1 << i; // 将第i位记为1
            }
        }

        return new State(res, slides.size()); // 构造一个State
    }

    // 读取state中index位置上的滑块信息
    public int stateToSlide(int index) {
        int res = this.state >> ((this.num - index - 1) * 4) & 15;
        return res + 1;
    }

    // 在state中修改index位置上滑块的位置信息为value
    public void setStateSlide(int index, int value) {
        value--;
        int base = this.num - index - 1;
        for (int i = 0; i < 4; i++) {
            if (((value >> i) & 1) == 1) {
                this.state |= (1 << (base * 4 + i)); // 改成1
            } else {
                this.state &= ~(1 << (base * 4 + i)); // 改成0
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
