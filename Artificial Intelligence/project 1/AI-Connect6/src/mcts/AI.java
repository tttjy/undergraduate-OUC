package mcts;

import core.board.Board;
import core.board.PieceColor;
import core.game.Game;
import core.game.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mcts.AI.AVAILABLE_CHOICE_NUMBER;
import static mcts.AI.MAX_ROUND_NUMBER;

/*ԭ��

�ڵ�ǰ���ڵ�(��ΪA)״̬�£���������ӽڵ㶼չ���ˣ���UCT�㷨ѡ�����Žڵ���Ϊ��ǰ�ڵ㣬ѭ����ȥ��ֱ���ýڵ���δչ�����ӽڵ㣬
���δչ�����ӽڵ���Ϲѡһ����չ��������ΪB������B��ʼ����ģ�⣨����ȥֱ����Ϸ���������õ��ô�ģ���Reward, ��B��ʼ���ϻ��ݣ�һֱ��A����
��;�ۼ��ϸô�Reward��ģ�����1��

���ϲ��裬��A״̬�¿��ظ��ܶ��ֱ����ʱΪֹ����ʱ��A�������ӽڵ���ѡ��ʤ����ߵ�һ����UCT��ʽ�����ߣ�����һ�������Ѹ��ӽڵ���ΪA��;

����ѭ��ִ�о���һ��������ȥ��*/
class State {
    /*���ؿ�������������Ϸ״̬����¼��ĳһ��Node�ڵ��µ�״̬���ݣ�������ǰ����Ϸ�÷֡���ǰ����Ϸround�����ӿ�ʼ����ǰ��ִ�м�¼��
  ��Ҫʵ���жϵ�ǰ״̬�Ƿ�ﵽ��Ϸ����״̬��֧�ִ�Action���������ȡ��������*/
    private double currentValue;
    private int currentRoundIndex;
    private List<Integer> cumulativeChoices;
    private PieceColor ourcolor;
    private  BoardMaster currentboard;
    private Move bestMove;
    ArrayList<MoveMaster> moveOrder = new ArrayList<>();

    public State(PieceColor color,BoardMaster board) {
        this.currentValue = 0.0;
        this.currentRoundIndex = 0;
        this.cumulativeChoices = new ArrayList<>();
        this.ourcolor = color;
        this.currentboard = board;
    }

    public BoardMaster getCurrentboard(){
        return currentboard;
   }

    public void setCurrentboard(BoardMaster currentboard) {
        this.currentboard = currentboard;
    }
    public Move getBestMove(){
        moveOrder.sort(MoveMaster.scoreComparator);
        bestMove = moveOrder.get(0);
        return bestMove;
    }
    public void setBestMove(Move bestMove){
        this.bestMove = bestMove;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double value) {
        this.currentValue = value;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public void setCurrentRoundIndex(int turn) {
        this.currentRoundIndex = turn;
    }

    public List<Integer> getCumulativeChoices() {
        return cumulativeChoices;
    }

    public void setCumulativeChoices(List<Integer> choices) {
        this.cumulativeChoices = choices;
    }

    public boolean isTerminal() {
        return this.currentRoundIndex == MAX_ROUND_NUMBER;
    }

    public double computeReward() {
        return -Math.abs(1 - this.currentValue);
    }
    public boolean DTSS(int depth){
        if(depth == 0)
            return false;
        if(ourcolor == currentboard.whoseMove()){
            if(currentboard.countAllThreats(ourcolor) > 0 && currentboard.countAllThreats(ourcolor.opposite()) == 0)
                return false;
            ArrayList<MoveMaster> movesList = currentboard.findDoubleThreats();
            for (MoveMaster move : movesList) {
                currentboard.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size() - 1);
                currentboard.undoMove(move);
                if (flag)
                    return true;
            }
            return false;
        }
        else {
            if(currentboard.countAllThreats(currentboard.whoseMove()) > 2){
                bestMove = moveOrder.get(0);
                return true;
            }
            ArrayList<MoveMaster> movesList = currentboard.findDoubleBlocks();
            for(MoveMaster move : movesList){
                currentboard.makeMove(move);
                moveOrder.add(move);
                boolean flag = DTSS(depth - 1);
                moveOrder.remove(moveOrder.size()-1);
                currentboard.undoMove(move);
                if(!flag)
                    return false;
            }
            return true;
        }
    }
    public State getNextStateWithRandomChoice() {
        Random random = new Random();

        State nextState = new State(ourcolor,currentboard);
        moveOrder.clear();
        //����DTSS��ͷ��أ��������ģ��
        if(DTSS(this.currentRoundIndex)){
            currentboard.makeMove(bestMove);
            nextState.setBestMove(bestMove);
            nextState.setCurrentboard(currentboard);
            moveOrder.add((MoveMaster) bestMove);

        }else{
            ArrayList<MoveMaster> moves = currentboard.findGenerateMoves();
            int[] choice = {0};
            int i = 0;
            for(MoveMaster m : moves){
                choice[i] = m.getScore();
                i++;
                moveOrder.add(m);
            }
            int len = choice.length;
            int randomChoice = choice[random.nextInt(len)];
            nextState.setCurrentValue(this.currentValue + randomChoice);
            nextState.setCurrentRoundIndex(this.currentRoundIndex + 1);
            List<Integer> newCumulativeChoices = new ArrayList<>(this.cumulativeChoices);
            newCumulativeChoices.add(randomChoice);
            nextState.setCumulativeChoices(newCumulativeChoices);
            nextState.setCurrentboard(currentboard);
        }
        return nextState;
    }

    @Override
    public String toString() {
        return "State: " + hashCode() + ", value: " + this.currentValue + ", round: " + this.currentRoundIndex +
                ", choices: " + this.cumulativeChoices;
    }
}

class MCTNode {
    /*���ؿ��������������ṹ��Node�������˸��ڵ��ֱ�ӵ����Ϣ���������ڼ���UCB�ı���������qualityֵ��������Ϸѡ�����Node��State��*/

    private MCTNode parent;
    private List<MCTNode> children;
    private int visitTimes;
    private double qualityValue;
    private State state;

    public MCTNode() {
        this.parent = null;
        this.children = new ArrayList<>();
        this.visitTimes = 0;
        this.qualityValue = 0.0;
        this.state = null;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public MCTNode getParent() {
        return parent;
    }

    public void setParent(MCTNode parent) {
        this.parent = parent;
    }

    public List<MCTNode> getChildren() {
        return children;
    }

    public int getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(int times) {
        this.visitTimes = times;
    }

    public void visitTimesAddOne() {
        this.visitTimes += 1;
    }

    public double getQualityValue() {
        return qualityValue;
    }

    public void setQualityValue(double value) {
        this.qualityValue = value;
    }

    public void qualityValueAddN(double n) {
        this.qualityValue += n;
    }

    public boolean isAllExpand() {
        return this.children.size() == AVAILABLE_CHOICE_NUMBER;
    }

    public void addChild(MCTNode subMCTNode) {
        subMCTNode.setParent(this);
        this.children.add(subMCTNode);
    }

    @Override
    public String toString() {
        return "Node: " + hashCode() + ", Q/N: " + this.qualityValue + "/" + this.visitTimes + ", state: " + this.state;
    }
}


public class AI extends core.player.AI {
    public static final int[] AVAILABLE_CHOICES = {1, -1, 2, -2};
    public static final int AVAILABLE_CHOICE_NUMBER = AVAILABLE_CHOICES.length;
    public static final int MAX_ROUND_NUMBER = 10;

    private static MCTNode treePolicy(MCTNode MCTNode) {
        /*���ؿ�����������Selection��Expansion�׶Σ����뵱ǰ��Ҫ��ʼ�����Ľڵ㣨������ڵ㣩��
        ����exploration/exploitation�㷨������õ���Ҫexpend�Ľڵ㣬ע������ڵ���Ҷ�ӽ��ֱ�ӷ��ء�
        �������������ҵ�ǰδѡ������ӽڵ㣬����ж�������ѡ�������ѡ�������Ȩ���exploration/exploitation��UCBֵ���ģ����UCBֵ��������ѡ��*/
        while (!MCTNode.getState().isTerminal()) {
            if (MCTNode.isAllExpand()) {
                MCTNode = bestChild(MCTNode, true);
            } else {
                MCTNode subMCTNode = expand(MCTNode);
                return subMCTNode;
            }
        }
        return MCTNode;
    }

    private static double defaultPolicy(MCTNode MCTNode) {
        /*���ؿ�����������Simulation�׶Σ�����һ����Ҫexpand�Ľڵ㣬��������󴴽��µĽڵ㣬���������ڵ��reward��
        ע������Ľڵ�Ӧ�ò����ӽڵ㣬��������δִ�е�Action����expend�ġ��������������ѡ��Action��*/
        State currentState = MCTNode.getState();
        while (!currentState.isTerminal()) {
            currentState = currentState.getNextStateWithRandomChoice();
        }
        double finalStateReward = currentState.computeReward();
        return finalStateReward;
    }

    private static MCTNode expand(MCTNode MCTNode) {
        /*����һ���ڵ㣬�ڸýڵ�����չһ���µĽڵ㣬ʹ��random����ִ��Action�����������Ľڵ㡣ע�⣬��Ҫ��֤�����Ľڵ��������ڵ�Action��ͬ��*/
        List<State> triedSubNodeStates = new ArrayList<>();
        for (MCTNode subMCTNode : MCTNode.getChildren()) {
            triedSubNodeStates.add(subMCTNode.getState());
        }
        State newState = MCTNode.getState().getNextStateWithRandomChoice();
        while (triedSubNodeStates.contains(newState)) {
            newState = MCTNode.getState().getNextStateWithRandomChoice();
        }
        MCTNode subMCTNode = new MCTNode();
        subMCTNode.setState(newState);
        MCTNode.addChild(subMCTNode);
        return subMCTNode;
    }

    private static MCTNode bestChild(MCTNode MCTNode, boolean isExploration) {
        /*ʹ��UCB�㷨��Ȩ��exploration��exploitation��ѡ��÷���ߵ��ӽڵ㣬ע�������Ԥ��׶�ֱ��ѡ��ǰQֵ�÷���ߵġ�*/
        double bestScore = Double.NEGATIVE_INFINITY;
        MCTNode bestSubMCTNode = null;
        for (MCTNode subMCTNode : MCTNode.getChildren()) {
            double C;
            if (isExploration) {
                C = 1 / Math.sqrt(2.0);
            } else {
                C = 0.0;
            }
            double left = subMCTNode.getQualityValue() / subMCTNode.getVisitTimes();
            double right = 2.0 * Math.log(MCTNode.getVisitTimes()) / subMCTNode.getVisitTimes();
            double score = left + C * Math.sqrt(right);
            if (score > bestScore) {
                bestSubMCTNode = subMCTNode;
                bestScore = score;
            }
        }
        return bestSubMCTNode;
    }

    private static void backup(MCTNode MCTNode, double reward) {
        /*���ؿ�����������Backpropagation�׶Σ�����ǰ���ȡ��Ҫexpend�Ľڵ����ִ��Action��reward��������expend�ڵ���������нڵ㲢���¶�Ӧ���ݡ�*/
        while (MCTNode != null) {
            MCTNode.visitTimesAddOne();
            MCTNode.qualityValueAddN(reward);
            MCTNode = MCTNode.getParent();
        }
    }

    private static MCTNode monteCarloTreeSearch(MCTNode MCTNode, BoardMaster board, PieceColor color) {
        /*ʵ�����ؿ����������㷨������һ�����ڵ㣬�����޵�ʱ���ڸ���֮ǰ�Ѿ�̽���������ṹexpand�½ڵ�͸������ݣ�Ȼ�󷵻�ֻҪexploitation��ߵ��ӽڵ㡣
          ���ؿ��������������ĸ����裬Selection��Expansion��Simulation��Backpropagation��
          ǰ����ʹ��tree policy�ҵ�ֵ��̽���Ľڵ㡣
          ������ʹ��default policyҲ������ѡ�еĽڵ�������㷨ѡһ���ӽڵ㲢����reward��
          ���һ��ʹ��backupҲ���ǰ�reward���µ����о�����ѡ�нڵ�Ľڵ��ϡ�
          ����Ԥ��ʱ��ֻ��Ҫ����Qֵѡ��exploitation���Ľڵ㼴�ɣ��ҵ���һ�����ŵĽڵ㡣*/
        int computationBudget = 2;
        for (int i = 0; i < computationBudget; i++) {
            MCTNode expandMCTNode = treePolicy(MCTNode);
            double reward = defaultPolicy(expandMCTNode);
            backup(expandMCTNode, reward);
        }
        MCTNode bestNextMCTNode = bestChild(MCTNode, false);
        board =MCTNode.getState().getCurrentboard();
        board.makeMove(MCTNode.getState().getBestMove());
        return bestNextMCTNode;
    }

    @Override
    public String name() {
        return "mcts";
    }

    //public Board board = new Board();

    public Board setBoard(Board board) {
        return null;
    }

    public Board getBoard() {
        return null;
    }
    private int steps = 0;
    private Move bestMove;
    PieceColor color;
    ArrayList<MoveMaster> moveOrder = new ArrayList<>();
    private BoardMaster board = null;

    @Override
    public Move findMove(Move opponentMove){
        if (opponentMove == null) {
            Move move = this.firstMove();
            this.board.makeMove(move);
            return move;
        } else {
            this.board.makeMove(opponentMove);

        }
        color = board.whoseMove();
        bestMove = board.findwinMoves();
        if(bestMove != null){
            board.makeMove(bestMove);
            return bestMove;
        }
        moveOrder.clear();
        State initState = new State(color,board);
        MCTNode initMCTNode = new MCTNode();
        initMCTNode.setState(initState);
        MCTNode currentMCTNode = initMCTNode;
        MCTNode moveNode = new MCTNode();
        double bestscore = 0.0;
        for (int j = 0; j < 10; j ++) {
            currentMCTNode = monteCarloTreeSearch(currentMCTNode,board,color);
            if(bestscore < currentMCTNode.getQualityValue()){
                bestscore = currentMCTNode.getQualityValue();
                moveNode = currentMCTNode;
            }
        }
        bestMove = moveNode.getState().getBestMove();
        if(bestMove == null){
            ArrayList<MoveMaster> moves = board.findGenerateMoves();
            moves.sort(MoveMaster.scoreComparator);
            bestMove = moves.get(0);
        }
        board.makeMove(bestMove);
        return bestMove;

    }
    @Override
    public void playGame(Game game) {
        super.playGame(game);
        board = new BoardMaster();
        steps = 0;
    }
}


