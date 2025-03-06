package stud.g01.problem.npuzzle;

import java.util.EnumMap;
import java.util.List;

/**
 * 地图中可以移动的8个方向，及其箭头符号
 */
public enum Direction {
    N('↑'),  //北
    W('←'), //西
    S('↓'), //南
    E('→'); //东


    private final char symbol;

    /**
     * 构造函数
     *
     * @param symbol 枚举项的代表符号--箭头
     */
    Direction(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static final int SCALE = 1;       //单元格的边长
    public static final double ROOT2 = 1.4;   //2的平方根


    public static final List<Direction> FOUR_DIRECTIONS = List.of(Direction.N, Direction.W, Direction.S, Direction.E);


    //各个方向移动的坐标位移量
    private static final EnumMap<Direction, int[]> DIRECTION_OFFSET = new EnumMap<>(Direction.class);

    static {
        //列号（或横坐标）增加量；行号（或纵坐标）增加量
        DIRECTION_OFFSET.put(N, new int[]{-1, 0});
        DIRECTION_OFFSET.put(W, new int[]{0, -1});
        DIRECTION_OFFSET.put(S, new int[]{1, 0});
        DIRECTION_OFFSET.put(E, new int[]{0, 1});
    }

    public static int[] offset(Direction dir) {
        return DIRECTION_OFFSET.get(dir);
    }
}
