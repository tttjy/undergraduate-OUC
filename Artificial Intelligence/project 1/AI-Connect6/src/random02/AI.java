package random02;

import core.board.Board;
import core.board.PieceColor;
import core.game.Game;
import core.game.Move;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class AI extends core.player.AI {
    private int steps = 0;

    @Override
    public String name() {
        return "random02";
    }

    public Board board = new Board();

    public Board setBoard(Board board) {
        return null;
    }

    public Board getBoard() {
        return null;
    }

    @Override
    public void playGame(Game game) {
        super.playGame(game);
        board = new Board();
    }

    // �����ڵ�8��λ����ѡȡһ����λ����
    public Pair<Integer, Integer> getAdjacentPosition(Pair<Integer, Integer> pos) {
        int col = pos.getKey();
        int row = pos.getValue();
        // �洢Ϊ��λ������λ��
        ArrayList<Pair<Integer, Integer>> adjacent = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int tmp_col = col + i;
                int tmp_row = row + j;
                // �жϸ�λ���Ƿ���������
                if (tmp_col >= 0 && tmp_col <= 18 && tmp_row >= 0 && tmp_row <= 18) {
                    adjacent.add(new Pair<>(tmp_col, tmp_row));
                }
            }
        }
        // �ж�����λ���Ƿ�������
        if (adjacent.isEmpty()) {
            return null;
        } else { // ���ѡһ������λ��
            int idx = new Random().nextInt(adjacent.size());
            return adjacent.get(idx);
        }
    }

    @Override
    public Move findMove(Move opponentMove) {
        if (opponentMove == null) {
            Move move = this.firstMove();
            this.board.makeMove(move);
            return move;
        } else {
            this.board.makeMove(opponentMove);

            char col0, row0, col1, row1;
            int begin = 0, end = 18;
            Pair<Integer, Integer> pos0, pos1; // ��һ���Ӻ͵ڶ����ӵ�����
            // ȷ������
            do {
                pos0 = getRandomPosition(begin, end);
                Pair<Integer, Integer> tmp = getAdjacentPosition(pos0);

                if (tmp == null) {
                    pos1 = getRandomPosition(begin, end);
                } else {
                    pos1 = tmp;
                }
                // ת��Ϊ����
                col0 = (char) (pos0.getKey() + 'A');
                row0 = (char) (pos0.getValue() + 'A');
                col1 = (char) (pos1.getKey() + 'A');
                row1 = (char) (pos1.getValue() + 'A');
            } while (this.board.get(col0, row0) != PieceColor.EMPTY || this.board.get(col1, row1) != PieceColor.EMPTY);

            Move move = new Move(col0, row0, col1, row1);
            this.board.makeMove(move);
            steps++;
            return move;
        }
    }
}