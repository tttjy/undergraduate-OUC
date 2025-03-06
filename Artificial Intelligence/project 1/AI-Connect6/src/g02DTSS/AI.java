package g02DTSS;

import core.board.Board;
import core.board.PieceColor;
import core.game.Game;
import core.game.Move;
import java.util.Random;
import java.util.ArrayList;

public class AI extends core.player.AI {
    PieceColor color;
    private Move bestMove;
    // ���������
    private static final int MAX_DEPTH = 3;
    /* ��¼һ����������� */
    ArrayList<MoveMaster> moveOrder = new ArrayList<>();
    private int steps = 0;

    @Override
    public Move findMove(Move opponentMove) {
        if (opponentMove == null) {
            Move move = firstMove();
            this.board.makeMove(move);
            return move;
        } else {
            board.makeMove(opponentMove);
        }

        bestMove = board.findwinMoves();
        if(bestMove != null){
            board.makeMove(bestMove);
            return bestMove;
        }
        //��¼�ҷ�����ɫ
        color = board.whoseMove();
        //���ص������㿪ʼ
        moveOrder.clear();
        for(int i = 3; i <= 27; i += 2){
            if(DTSS(i)){
                board.makeMove(bestMove);
                return bestMove;
            }
        }
        //alphaBeta��֦����
        alphaBeta(-Integer.MAX_VALUE, Integer.MAX_VALUE,1, MAX_DEPTH);

        if(bestMove == null){
            ArrayList<MoveMaster> moves = board.findGenerateMoves();
            moves.sort(MoveMaster.scoreComparator);
            bestMove = moves.get(0);
        }
        board.makeMove(bestMove);
        return bestMove;
    }
//@Override
//public Move findMove(Move opponentMove) {
//    if (opponentMove == null) {
//        return makeFirstMove();
//    } else {
//        board.makeMove(opponentMove);
//    }
//
//    bestMove = board.findwinMoves();
//    if (bestMove != null) {
//        return makeAndReturnMove(bestMove);
//    }
//
//    color = board.whoseMove();
//    moveOrder.clear();
//
//    for (int depth = 3; depth <= 27; depth += 2) {
//        if (DTSS(depth)) {
//            return makeAndReturnMove(bestMove);
//        }
//    }
//
//    alphaBeta(-Integer.MAX_VALUE, Integer.MAX_VALUE, 1, MAX_DEPTH);
//
//    if (bestMove == null) {
//        ArrayList<MoveMaster> moves = board.findGenerateMoves();
//        moves.sort(MoveMaster.scoreComparator);
//        bestMove = moves.get(0);
//    }
//
//    return makeAndReturnMove(bestMove);
//}
//
//    private Move makeFirstMove() {
//        Move move = firstMove();
//        board.makeMove(move);
//        return move;
//    }
//
//    private Move makeAndReturnMove(Move move) {
//        board.makeMove(move);
//        return move;
//    }

    boolean DTSS(int depth){
        // depthΪ0�������ﵽ�����Ȼ�û���ҵ�����˫���ŵ������return false��
        //������̽���ҷ��ԶԷ���˫в��
        if(depth == 0)
            return false;
        //���ҷ�����ʱ
        if(color == board.whoseMove()){
            //����Է������ҷ�������в�������ҷ����ڶԷ�û����в
            if(board.countAllThreats(color) > 0 && board.countAllThreats(color.opposite()) == 0)
                return false;
            //�ҵ��ҷ������Ϊ˫��в�������ŷ�
            ArrayList<MoveMaster> movesList = board.findDoubleThreats();
            for (MoveMaster move : movesList) {
                board.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size() - 1);
                board.undoMove(move);
                // �����㷨�����ڼ��ɷ���true
                if (flag)
                    return true;
            }
            return false;
        }

        //���Է�����ʱ
        else {
            //����²�ס�ҷ����ڶԷ�����в
            if(board.countAllThreats(board.whoseMove()) > 2){
                bestMove = moveOrder.get(0);
                return true;
            }
            //�ҵ��Է��������ҷ��������ŷ�
            ArrayList<MoveMaster> movesList = board.findDoubleBlocks();
            for(MoveMaster move : movesList){
                board.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size()-1);
                board.undoMove(move);
                //�����㷨������ȫ�����Գ���˫��в����������ʧ��
                //�Է�����ÿ����в���ܶ�ס�������ҷ�ʤ������true
                if(!flag)
                    return false;
            }
            return true;
        }
    }
    //���׶�MCTS����
    /*void MCTS(){
        while(){
            
        }
    }*/
    public int alphaBeta(int alpha,int beta, int nw, int depth) {
        if (board.gameOver() || depth <= 0) {
            int evaluateScore = RoadTable.evaluateChessStatus(color, board.getRoadTable());
            return evaluateScore;
        }
        ArrayList<MoveMaster> moves = null;
        int threats = board.countAllThreats(board.whoseMove());
        if (threats == 0) {
            moves = board.findGenerateMoves();
        } else if (threats == 1) {
            moves = board.findSingleBlocks();
        } else if (threats == 2) {
            moves = board.findDoubleBlocks();
        } else {
            moves = board.findTripleBlocks();
        }

        if(nw==1){
            // ����ʽ����
            int tAlpha;
            moves.sort(MoveMaster.scoreComparator);
            for (MoveMaster move : moves) {

                board.makeMove(move);
                tAlpha = alphaBeta(alpha, beta,0, depth - 1);
                board.undoMove(move);

                if(tAlpha > alpha){
                    alpha = tAlpha;
                    if (depth == MAX_DEPTH){
                        board.makeMove(move);
                        color = color.opposite();
                        moveOrder.clear();
                        // ��һ������DTSS���� ��ֹ�Լ�����ʧ��
                        if (!DTSS(7)) {
                            bestMove = move;
                        }

                        color = color.opposite();
                        board.undoMove(move);
                    }
                }
                if(alpha >= beta){
                    return beta;
                }
            }
            return alpha;
        }else{
            //min
            // ����ʽ����
            int tBeta;
            moves.sort(MoveMaster.scoreComparator);
            for (MoveMaster move : moves) {
                board.makeMove(move);
                tBeta = alphaBeta(alpha, beta,1, depth - 1);
                board.undoMove(move);
                if(beta > tBeta){
                    beta = tBeta;
                }
                if(alpha >= beta){
                    return alpha;
                }
            }
            return beta;
        }

    }

//    public int alphaBeta(int alpha, int beta, int nw, int depth) {
//        if (board.gameOver() || depth <= 0) {
//            return RoadTable.evaluateChessStatus(color, board.getRoadTable());
//        }
//
//        ArrayList<MoveMaster> moves = getMovesAccordingToThreats();
//        int bestScore = (nw == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//
//        // Heuristic sorting
//        moves.sort(MoveMaster.scoreComparator);
//
//        for (MoveMaster move : moves) {
//            board.makeMove(move);
//            int score = alphaBeta(alpha, beta, (nw == 1) ? 0 : 1, depth - 1);
//            board.undoMove(move);
//
//            if (nw == 1) { // Maximize
//                if (score > alpha) {
//                    alpha = score;
//                    if (depth == MAX_DEPTH && !DTSS(7)) {
//                        updateBestMove(move);
//                    }
//                }
//                if (alpha >= beta) {
//                    return beta;
//                }
//            } else { // Minimize
//                if (score < beta) {
//                    beta = score;
//                }
//                if (alpha >= beta) {
//                    return alpha;
//                }
//            }
//        }
//
//        return (nw == 1) ? alpha : beta;
//    }
//
//    private ArrayList<MoveMaster> getMovesAccordingToThreats() {
//        int threats = board.countAllThreats(board.whoseMove());
//        if (threats == 0) {
//            return board.findGenerateMoves();
//        } else if (threats == 1) {
//            return board.findSingleBlocks();
//        } else if (threats == 2) {
//            return board.findDoubleBlocks();
//        } else {
//            return board.findTripleBlocks();
//        }
//    }
//
//    private void updateBestMove(MoveMaster move) {
//        board.makeMove(move);
//        color = color.opposite();
//        moveOrder.clear();
//        if (!DTSS(7)) {
//            bestMove = move;
//        }
//        color = color.opposite();
//        board.undoMove(move);
//    }


    @Override
    public String name() {
        return "G02DTSS";
    }


    public Board setBoard(Board board) {
        return null;
    }

    public Board getBoard() {
        return null;
    }

    @Override
    public void playGame(Game game) {
        super.playGame(game);
        board = new BoardMaster();
    }
    //�Լ����е�����
    private BoardMaster board = null;
}
