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
    // 搜索的深度
    private static final int MAX_DEPTH = 3;
    /* 记录一下行棋的序列 */
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
        //记录我方的颜色
        color = board.whoseMove();
        //搜素到第三层开始
        moveOrder.clear();
        for(int i = 3; i <= 27; i += 2){
            if(DTSS(i)){
                board.makeMove(bestMove);
                return bestMove;
            }
        }
        //alphaBeta剪枝搜索
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
        // depth为0，搜索达到最大深度还没有找到连续双迫着的情况，return false；
        //核心是探索我方对对方的双胁迫
        if(depth == 0)
            return false;
        //当我方行棋时
        if(color == board.whoseMove()){
            //如果对方对于我方存在威胁，但是我方对于对方没有威胁
            if(board.countAllThreats(color) > 0 && board.countAllThreats(color.opposite()) == 0)
                return false;
            //找到我方行棋成为双威胁的所有着法
            ArrayList<MoveMaster> movesList = board.findDoubleThreats();
            for (MoveMaster move : movesList) {
                board.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size() - 1);
                board.undoMove(move);
                // 根据算法，存在即可返回true
                if (flag)
                    return true;
            }
            return false;
        }

        //当对方行棋时
        else {
            //如果堵不住我方对于对方的威胁
            if(board.countAllThreats(board.whoseMove()) > 2){
                bestMove = moveOrder.get(0);
                return true;
            }
            //找到对方用来堵我方的所有着法
            ArrayList<MoveMaster> movesList = board.findDoubleBlocks();
            for(MoveMaster move : movesList){
                board.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size()-1);
                board.undoMove(move);
                //根据算法，必须全部可以出现双威胁，否则搜索失败
                //对方必须每个威胁都能堵住，否则我方胜，返回true
                if(!flag)
                    return false;
            }
            return true;
        }
    }
    //两阶段MCTS搜索
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
            // 启发式排序
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
                        // 加一步反向DTSS搜索 防止自己防御失误
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
            // 启发式排序
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
    //自己保有的棋盘
    private BoardMaster board = null;
}
