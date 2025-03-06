package stud.g01.DB;

import java.util.ArrayList;
import java.util.HashMap;

public class Block {
    // ��̬������block ����
    public static int num = 0;
    public final static int Size = 4;

    // ��̬�������û������ڵڼ���block
    public static ArrayList<Integer> belongs;
    // ��̬���캯��
    static {
        belongs = new ArrayList<>();
        for (int i = 0; i < Size * Size; i++) {
            belongs.add(0);
        }
    }

    // ������block �����
    public final int order;
    // ������һ��block�еĻ�������
    private final int size;
    // ������һ��block�еĻ���
    private final ArrayList<Integer> slides;
    // ����ӳ���ϵ��slides[i]������
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

    // �жϻ����Ƿ���block��
    public boolean isSlideIn(int slide) {
        return Index.containsKey(slide);
    }

    public void Print() {
        for (int i = 0; i < size; i++) {
            System.out.print(slides.get(i) + " ");
        }
        System.out.println();
    }

    // ���캯��
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
