package random03;

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
        return "random03";
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

    @Override
    public Move findMove(Move opponentMove) {
        if (opponentMove == null) {
            Move move = this.firstMove();
            this.board.makeMove(move);
            return move;
        } else {
            this.board.makeMove(opponentMove);

            final int CENTER_LENGTH = 13; // 中心区域长度
            final int TIMES_LIMIT = 10; // 次数限制
            char col0, row0, col1, row1;
            int begin = (19 - CENTER_LENGTH) / 2;
            int end = (19 - CENTER_LENGTH) / 2 + CENTER_LENGTH - 1;
            Pair<Integer, Integer> pos0, pos1;

            int cnt = 0;
            do {
                if (cnt >= TIMES_LIMIT) { // 若超过次数限制则在全局范围内随机掷骰子
                    begin = 0;
                    end = 18;
                }
                do {
                    pos0 = getRandomPosition(begin, end);
                    pos1 = getRandomPosition(begin, end);
                } while (pos0 == pos1);
                ++cnt;
                // 转换为棋盘
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