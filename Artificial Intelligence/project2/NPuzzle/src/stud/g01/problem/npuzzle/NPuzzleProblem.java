package stud.g01.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.EnumMap;

public class NPuzzleProblem extends Problem {


    public NPuzzleProblem(State initialState, State goal) {
        super(initialState, goal);
    }

    public NPuzzleProblem(State initialState, State goal, int size) {
        super(initialState, goal, size);
    }

    static int flag = 0;
    @Override
    public void savePathToCustomFile(String filePath, Deque<Node> solutionPath, Problem problem, int size) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            NPuzzleState initialState = (NPuzzleState) ((NPuzzleProblem) problem).getInitialState();
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write the size of the initial state
            printWriter.print(initialState.getSize() + " ");

            // Write the initial state values
            for (int i = 0; i < initialState.size * initialState.size; i++) {
                int row = i / initialState.size;
                int col = i % initialState.size;
                printWriter.print(initialState.state[row][col] + " ");
            }

            // Write the size of the solution path
            printWriter.print(solutionPath.size() + " ");

            NPuzzleState previousState = initialState;
            printWriter.print(solutionPath.size() + " ");

            for (var step : solutionPath) {
                NPuzzleState currentState = (NPuzzleState) step.getState();
                int rowDiff = previousState.getRow() - currentState.getRow();
                int colDiff = previousState.getCol() - currentState.getCol();

                char moveDirection = getMoveDirection(rowDiff, colDiff);
                printWriter.print(moveDirection + " ");

                previousState = currentState;
            }

            printWriter.println();
            fileWriter.flush();
            fileWriter.close();
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char getMoveDirection(int rowDiff, int colDiff) {
        if (rowDiff == 0 && colDiff == 1) {
            return '左'; // Left
        } else if (rowDiff == 0 && colDiff == -1) {
            return '右';// Right
        } else if (rowDiff == 1 && colDiff == 0) {
            return '上'; // Up
        } else if (rowDiff == -1 && colDiff == 0) {
            return '下'; // Down
        }
        return ' ';
    }


    @Override
    public boolean solvable() {
        byte[][] state = ((NPuzzleState) getInitialState()).getState();
        int count = getReverseCount(state);
        if (size % 2 == 1) { //size为奇数,逆序对为偶数才有解
            return (count % 2 == 0);
        } else { //size为偶数
            if ((size - ((NPuzzleState) getInitialState()).getRow()) % 2 == 1) {  //空格在奇数行
                return (count % 2 == 0);
            } else { //空格在偶数行
                return (count % 2 == 1);
            }
        }
    }

    private int getReverseCount(byte[][] state) {
        int res = 0;
        for (int i = 0; i < size * size - 1; i++) {
            for (int j = i + 1; j < size * size; j++) {
                int x1 = i / size, y1 = i % size, x2 = j / size, y2 = j % size;
                if (state[x2][y2] == 0) continue;
                if (state[x2][y2] < state[x1][y1]) res++;
            }
        }
        return res;
    }

    public State getInitialState() {
        return initialState;
    }

    @Override
    public int stepCost(State state, Action action) {
        return 1;
    }

    @Override
    public boolean applicable(State state, Action action) {
        int[] offsets = Direction.offset(((Move) action).getDirection());
        int row = ((NPuzzleState) state).getRow() + offsets[0];
        int col = ((NPuzzleState) state).getCol() + offsets[1];
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    @Override
    public void showSolution(Deque<Node> path) {

    }
}
