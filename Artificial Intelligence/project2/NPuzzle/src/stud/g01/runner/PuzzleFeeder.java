package stud.g01.runner;

import core.problem.Problem;
import core.runner.EngineFeeder;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.g01.problem.npuzzle.NPuzzleProblem;
import stud.g01.problem.npuzzle.NPuzzleState;

import java.util.ArrayList;

public class PuzzleFeeder extends EngineFeeder {

    @Override
    public ArrayList<Problem> getProblems(ArrayList<String> problemLines) {
        ArrayList<Problem> problems = new ArrayList<>();
        for (String problemLine : problemLines) {
            problems.add(createNPuzzle(problemLine));
        }
        return problems;
    }

    private NPuzzleProblem createNPuzzle(String problemLine) {
        String[] strings = problemLine.split(" ");
        int size = Integer.parseInt(strings[0]); //输入的第一个数表示size
        byte[] initial_state = new byte[size * size];
        byte[] goal = new byte[size * size];
        for (int i = 1; i <= size * size; i++) {
            initial_state[i - 1] = (byte) Integer.parseInt(strings[i]);
            goal[i - 1] = (byte) Integer.parseInt(strings[i + size * size]);
        }

        NPuzzleState initialState = new NPuzzleState(size, initial_state, true);
        NPuzzleState goalState = new NPuzzleState(size, goal, false);
        return new NPuzzleProblem(initialState, goalState, size);
    }

    @Override
    public Predictor getPredictor(HeuristicType type) {
        return NPuzzleState.predictor(type);
    }
}

