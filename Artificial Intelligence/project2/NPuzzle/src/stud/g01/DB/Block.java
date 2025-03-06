package stud.g01.DB;

import java.util.ArrayList;
import java.util.HashMap;

public class Block {
    // 静态变量，block 总数
    public static int num = 0;
    public final static int Size = 4;

    // 静态变量，该滑块属于第几个block
    public static ArrayList<Integer> belongs;
    // 静态构造函数
    static {
        belongs = new ArrayList<>();
        for (int i = 0; i < Size * Size; i++) {
            belongs.add(0);
        }
    }

    // 常量，block 的序号
    public final int order;
    // 常量，一个block中的滑块数量
    private final int size;
    // 常量，一个block中的滑块
    private final ArrayList<Integer> slides;
    // 建立映射关系，slides[i]的索引
    private final HashMap<Integer, Integer> Index;

    public int getSize(){
        return size;
    }

    public int getSlide(int index) {
        return slides.get(index);
    }

    public int getIndex(int slide) {
        return Index.get(slide);
    }

    public ArrayList<Integer> getSlides() {
        return new ArrayList<>(this.slides);
    }

    // 判断滑块是否在block中
    public boolean isSlideIn(int slide) {
        return Index.containsKey(slide);
    }

    public void Print() {
        for (int i = 0; i < size; i++) {
            System.out.print(slides.get(i) + " ");
        }
        System.out.println();
    }

    // 构造函数
    public Block(int... slides) {
        this.order = Block.num;
        Block.num++;
        size = slides.length;
        this.slides = new ArrayList<>();
        this.Index = new HashMap<>();

        for (int i = 0; i < size; i++) {
            this.slides.add(slides[i]);
            this.Index.put(slides[i], i);
            Block.belongs.set(slides[i], this.order);
        }
    }
}
