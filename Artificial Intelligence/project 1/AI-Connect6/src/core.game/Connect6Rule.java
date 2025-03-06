package core.game;

import static core.board.PieceColor.EMPTY;

import core.board.Board;
import core.board.PieceColor;

/**
 * @author ����
 *
 */
public class Connect6Rule {
    Board _board;
    public static final int[][] FORWARD = { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, -1 } };

    public Connect6Rule(Board _board){
        this._board = _board;
    }
    // �Ƿ�Ϸ��߲�
    public boolean legalMove(Move mov) {
        return Move.validSquare(mov.index1()) && _board.get(mov.index1()) == EMPTY && Move.validSquare(mov.index2())
                && _board.get(mov.index2()) == EMPTY && mov.index1() != mov.index2(); // ͬһ�߲��������������ڲ�ͬ��
    }

    public boolean gameOver() {
        if (_board.getMoveList().isEmpty())
            return false;
        Move lastMove = _board.getMoveList().get(_board.getMoveList().size() - 1);

        return isWin(lastMove.col0(), lastMove.row0()) || isWin(lastMove.col1(), lastMove.row1());
    }

    // �����ڵ�(col��row)�����Ӻ󣬸������Ƿ��ʤ
    private boolean isWin(char col, char row) {

        for (int dir = 0; dir < 4; dir++) {
            // �ӵ�ǰ����dir����������ͬ��ɫ�ĵ�ĸ�����������ǰ��
            int len = lengthConnected(col, row, dir, 6);
            // �Ѿ�����
            if (len == 6) {
                return true;
            } else {
                // ���򣬴ӵ�ǰ�㣬��dir����ķ������˺�6-len�������λ��
                char startCol = (char) (col - FORWARD[dir][0] * (6 - len));
                char startRow = (char) (row - FORWARD[dir][1] * (6 - len));
                // ������ǺϷ��ĵ㣬������鿴��һ������
                if (!Move.validSquare(startCol, startRow)) {
                    continue;
                }
                int tempLen = 6 - len;
                len = lengthConnected(startCol, startRow, dir, tempLen);
                // �������
                if (len == tempLen) {
                    return true;
                }
            }
        }
        return false;
    }

    // �ӵ�(startCol, startRow)��ʼ�����ŷ���dir��ǰ��len��λ�ã�������õ���ɫ��ͬ�����ӵĸ���
    // �ø��� <= len.
    private int lengthConnected(char startCol, char startRow, int dir, int len) {

        int myLen = 0;
        // ��(startCol, startRow)�����ӵ���ɫ
        PieceColor myColor = _board.whoseMove().opposite();
        for (int j = 0; j < len; j++) {
            char tempCol = (char) (startCol + FORWARD[dir][0] * j);
            char tempRow = (char) (startRow + FORWARD[dir][1] * j);
            if (Move.validSquare(tempCol, tempRow) && _board.get(tempCol, tempRow) == myColor) {
                myLen++;
            } else {
                break;
            }
        }
        return myLen;
    }
}