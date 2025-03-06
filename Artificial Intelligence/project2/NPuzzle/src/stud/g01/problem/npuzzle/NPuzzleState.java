package stud.g01.problem.npuzzle;

import java.util.ArrayList;

import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.g01.DB.Block;
import stud.g01.DB.PatternDataBase;

import java.util.EnumMap;
import java.util.Collection;

import static core.solver.algorithm.heuristic.HeuristicType.*;

public class NPuzzleState extends State {
    public int size;
    public byte[][] state;

    public int row = 0;
    public int col = 0;
    public int Manhattan_distance = 0;
    public int Misplace = 0;
    public int hash = 0;
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    public static ArrayList<Block> blocks = new ArrayList<Block>() {
        {
            add(new Block(1, 5, 6, 9, 10, 13));
            add(new Block(7, 8, 11, 12, 14, 15));
            add(new Block(2, 3, 4));
        }
    };


    public int getSize() {
        return size;
    }

    public byte[][] getState() {
        return state;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setState(byte[][] state) {
        this.state = state;
    }


    public int getDataBaseDistance() {
        return Math.max(PatternDataBase.getDistance(state,blocks),getLinearConflictDistance());
    }

    public int getLinearConflictDistance() {
        return Manhattan_distance + 2 * linearConflict();
    }

    public int linearConflict() {
        int conflicts = 0;
        byte[][] state = this.getState();
        int size = state.length;


        // Check for horizontal linear conflicts
        for (int r = 0; r < size; r++) {
            for (int cl = 0; cl < size; cl++) {
                int valueCl = state[r][cl];
                if (valueCl == 0 || cl == size - 1) {
                    continue;  // Skip the empty tile and the last column
                }

                for (int cr = cl + 1; cr < size; cr++) {
                    int valueCr = state[r][cr];
                    if (valueCr == 0) {
                        continue;  // Skip the empty tile
                    }

                    int goalCl = getGoalColumn(valueCl, size);
                    int goalCr = getGoalColumn(valueCr, size);

                    if (r == getGoalRow(valueCl, size) && r == getGoalRow(valueCr, size)
                            && goalCl > goalCr) {
                        conflicts++;
                    }
                }
            }
        }

        // Check for vertical linear conflicts
        for (int c = 0; c < size; c++) {
            for (int rt = 0; rt < size; rt++) {
                int valueRt = state[rt][c];
                if (valueRt == 0 || rt == size - 1) {
                    continue;  // Skip the empty tile and the last row
                }

                for (int rb = rt + 1; rb < size; rb++) {
                    int valueRb = state[rb][c];
                    if (valueRb == 0) {
                        continue;  // Skip the empty tile
                    }

                    int goalRt = getGoalRow(valueRt, size);
                    int goalRb = getGoalRow(valueRb, size);

                    if (c == getGoalColumn(valueRt, size) && c == getGoalColumn(valueRb, size)
                            && goalRt > goalRb) {
                        conflicts++;
                    }
                }
            }
        }

        return conflicts;
    }

    // Helper method to get the goal row for a given value
    private static int getGoalRow(int value, int size) {
        return (value - 1) / size;
    }

    // Helper method to get the goal column for a given value
    private static int getGoalColumn(int value, int size) {
        return (value - 1) % size;
    }

    public NPuzzleState(int size, byte[] board, boolean flag) {
        this.size = size;
        this.state = new byte[size][size];
        fillStateBoard(board);
        initializeDistancesAndHash(flag);
    }

    private void initializeDistancesAndHash(boolean flag) {
        this.Manhattan_distance = 0; // 改为 this.Manhattan_distance
        this.Misplace = 0; // 改为 this.Misplace
        this.hash = 0; // 保持不变

        if (flag) {
            calculateManhattanDistance();
            calculateMisplacedDistance();
        }

        calculateZobristHash();
    }

    private void fillStateBoard(byte[] board) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.state[i][j] = board[i * size + j];
                if (this.state[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
    }

    private void calculateManhattanDistance() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] == 0) continue;
                Manhattan_distance += Math.abs((state[i][j] - 1) / size - i) + Math.abs((state[i][j] - 1) % size - j);
            }
        }
    }

    private void calculateMisplacedDistance() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] == 0 && i * size + j + 1 == size * size) continue;
                if (state[i][j] != i * size + j + 1) Misplace++;
            }
        }
    }

    private void calculateZobristHash() {
        for (int i = 0; i < size * size; i++) {
            if (state[i / size][i % size] != 0) {
                if (size == 4) {
                    hash ^= Zobrist.zobristHash4[i][state[i / size][i % size]];
                } else {
                    hash ^= Zobrist.zobristHash3[i][state[i / size][i % size]];
                }
            }
        }
    }

    public int getManhattan_distance() {
        return Manhattan_distance;
    }

    public int getMisplace() {
        return Misplace;
    }


    static {
        predictors.put(LINEAR_CONFLICT, (state, goal) -> ((NPuzzleState) state).getLinearConflictDistance());
        predictors.put(MANHATTAN, (state, goal) -> ((NPuzzleState) state).getManhattan_distance());
        predictors.put(MISPLACED, (state, goal) -> ((NPuzzleState) state).getMisplace());
        predictors.put(DISJOINT_PATTERN, (state, goal) -> ((NPuzzleState) state).getDataBaseDistance());
    }

    void setManhattan_distance(int manhattanDistance) {
        this.Manhattan_distance = manhattanDistance;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setMisplace(int misplace) {
        Misplace = misplace;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getHash() {
        return hash;
    }

    public static Predictor predictor(HeuristicType type) {
        return predictors.get(type);
    }

    public void draw() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] != 0) {
                    System.out.print(state[i][j] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public State next(Action action) {
        Direction direction = ((Move) action).getDirection();
        int[] offsets = Direction.offset(direction);
        int newRow = row + offsets[0];
        int newCol = col + offsets[1];

        NPuzzleState newState = new NPuzzleState(this);
        byte val = state[newRow][newCol];
        newState.state[row][col] = val;
        newState.state[newRow][newCol] = 0;

        newState.setCol(newCol);
        newState.setRow(newRow);

        int oldManhattan = computeManhattanDistance(val, newRow, newCol);
        int newManhattan = computeManhattanDistance(val, row, col);
        newState.setManhattan_distance(Manhattan_distance - oldManhattan + newManhattan);

        int displacementChange = computeDisplacementChange(val, newRow, newCol) - computeDisplacementChange(val, row, col);
        newState.setMisplace(Misplace + displacementChange);

//        int hashUpdate = computeHashUpdate(val, newRow, newCol) ^ computeHashUpdate(val, row, col);
//        newState.setHash(hash ^ hashUpdate);
        if (size == 4) {
            newState.setHash(hash ^ Zobrist.zobristHash4[newRow * size + newCol][val] ^ Zobrist.zobristHash4[row * size + col][val]);
        } else {
            newState.setHash(hash ^ Zobrist.zobristHash3[newRow * size + newCol][val] ^ Zobrist.zobristHash3[row * size + col][val]);
        }
        return newState;
    }

    private int computeManhattanDistance(byte val, int row, int col) {
        return Math.abs((val - 1) / size - row) + Math.abs((val - 1) % size - col);
    }

    private int computeDisplacementChange(byte val, int row, int col) {
        int change = 0;
        int currentTile = row * size + col + 1;
        int goalTile = size * size; // 目标数字
        if (currentTile == val) {
            change++;
        }
        if (currentTile == goalTile) {
            change--;
        }
        return change;
    }

    public NPuzzleState(NPuzzleState state) {
        this.size = state.getSize();
        this.state = new byte[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.state[i][j] = state.getState()[i][j];
            }
        }
    }

    public Iterable<? extends Action> actions() {
        Collection<Move> moves = new ArrayList<>();
        for (Direction d : Direction.FOUR_DIRECTIONS) {
            moves.add(new Move(d));
        }
        return moves;
    }

    public int hashCode() {
        return hash;
    }

//    public State moveAndCreateNewState(Action action) {
//        Direction moveDirection = ((Move) action).getDirection();
//        int[] moveOffsets = Direction.offset(moveDirection);
//        int newRowIndex = row + moveOffsets[0];
//        int newColIndex = col + moveOffsets[1];
//
//        NPuzzleState newState = new NPuzzleState(this);
//
//        byte movedValue = state[newRowIndex][newColIndex];
//        byte currentValue = state[row][col];
//
//        // Update the board state
//        newState.state[row][col] = movedValue;
//        newState.state[newRowIndex][newColIndex] = currentValue;
//
//        // Update the row and column positions
//        newState.setRow(newRowIndex);
//        newState.setCol(newColIndex);
//
//        // Update Manhattan distance
//        int oldManhattanDist = calculateManhattanDistance(currentValue, row, col);
//        int newManhattanDist = calculateManhattanDistance(movedValue, newRowIndex, newColIndex);
//        newState.setManhattan_distance(Manhattan_distance - oldManhattanDist + newManhattanDist);
//
//        // Update misplaced distance
//        int displacementChange = calculateDisplacementChange(row, col, movedValue, newRowIndex, newColIndex);
//        newState.setMisplace(Misplace + displacementChange);
//
////        // Update Zobrist hash
////        int hashChange = calculateZobristHashChange(newState, row, col, newRowIndex, newColIndex, currentValue, movedValue);
////        newState.setHash(hash ^ hashChange);
//
//        return newState;
//    }

    private int calculateManhattanDistance(byte value, int row, int col) {
        // Calculate Manhattan distance for a given value at a specific row and column
        int targetRow = (value - 1) / size;
        int targetCol = (value - 1) % size;
        return Math.abs(targetRow - row) + Math.abs(targetCol - col);
    }

    private int calculateDisplacementChange(int row, int col, byte value, int newRow, int newCol) {
        int displacementChange = 0;
        if (row * size + col + 1 == value) {
            displacementChange--;
        }
        if (row * size + col + 1 == this.size * this.size) {
            displacementChange++;
        }
        if (newRow * size + newCol + 1 == value) {
            displacementChange++;
        }
        if (newRow * size + newCol + 1 == this.size * this.size) {
            displacementChange--;
        }
        return displacementChange;
    }

    public boolean equals(Object obj) {
        NPuzzleState temp = (NPuzzleState) obj;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; ++j) {
                if (this.state[i][j] != temp.state[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


}
