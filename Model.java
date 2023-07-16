package ver3;

import java.util.ArrayList;

/**
 * Model of the Mancala Game
 * @author Ezra Tarab | 03/05/2023
 */
public class Model
{
    // Attributes תכונות
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    public static final int TIE_NUMBER = 3;
    public static final int MAX_DEPTH = 9;

    
    private Controller controller;
    private State currentState;
    // Methoods פעולות

    public Model(Controller controller)
    {
        this.controller = controller;
    }
    /**
     * פעולה לאתחול המודל
     */
    public void setup()
    {
        currentState = new State();
    }
    /**
     * פעולה המזומנת על מנת לבצע את המהלך
     * @param move - המהלך
     * @param playerSign - מספר השחקן
     * @return את הלוח המעודכן עם המהלך
     */
    public State makeMove(Move move,int playerSign){
        return move.makeMove(move, playerSign, currentState);
    }
    
    /**
     * פעולה הבודקת מי המנצח
     * @return את מספר השחקן המנצח
     */
    public int checkWin()
    {
        int winner =  currentState.checkWin();
        controller.updateSum1(currentState.getOpponentSum());
        controller.updateSum2(currentState.getPlayerSum());
        return winner;
    }
    /**
     * פעולה לבדיקה אם יש תיקו 
     * @return אם יש תיקו או לא
     */
    public boolean checkTie()
    {
        return checkTieMinimax(currentState);
    }
    
    /**
     * פעולת עזר למינימקס לבדיקת תיקו
     * @param state - הלוח
     * @return אם יש תיקו או לא
     */
    public boolean checkTieMinimax(State state)
    {
        return state.checkTie(state);
    }
    
    /**
     * פעולה הבודקת אם נגמר המשחק על ידי ניצחון או הפסד או תיקו
     * @return אם נגמר המשחק או לא
     */
    private boolean isGameOver()
    {
        return isGameOverMinimax(currentState);
    }
    
    /**
     * פעולת עזר למינימקס הבדוקת אם נגמר המשחק על ידי ניצחון או הפסד או תיקו
     * @param state - הלוח
     * @return אם נגמר המשחק או לא
     */
    private boolean isGameOverMinimax(State state)
    {
        if (checkTieMinimax(state))
            return true;
        if(state.checkCopyWin(state)!=0)
            return true;
        return false;
    }
    
    /**
     * פעולה המחזירה את מהלך השחקן הממוחשב
     * @return מהלך השחקן הממוחשב
     */
    public Move getAiMove()
    {
        System.out.println(">> Model.getAiMove() aiMove= " );
        Move AIMove = getMinimaxMove(currentState);
        return AIMove;
    }
    
    /**
     * פעולת עזר להחזרת השחקן הממוחשב
     * @param state - הלוח
     * @return מהלך השחקן הממוחשב
     */
    private Move getMinimaxMove(State state)
    {
        System.out.println();
        System.out.println("Start MINIMAX for Player " + controller.getCurrentPlayer());
        System.out.println(state);
        long startTime = System.currentTimeMillis();
        Move minimaxMove = minimax(state, 0, MAX_DEPTH, true);
        long finishTime = System.currentTimeMillis();
        
        System.out.println("MINIMAX finished with " + minimaxMove + ", total time: " + (finishTime-startTime) + "ms");
        return minimaxMove; 
    }
    
    /**
     * פעולה רקורסיבית לחישוב המהלך הטוב ביותר של השחקן הממוחשב
     * @param state - הלוח
     * @param depth - תמיד בהתחלה יהיה אפס זה פרמטר עזר לחישוב המהלך הטוב ביותר
     * @param maxDepth - פרמטר עזר לתנאי עצירת הפעולה
     * @param isMaxPlayer - אם אנו מחשבים בשביל השחקן המששחק או השחקן היריב
     * @return המהלך הטוב ביותר 
     */
    private Move minimax(State state, int depth, int maxDepth, boolean isMaxPlayer)
    {
        Move bestMove = null;
        
        if(isGameOverMinimax(state) || maxDepth == depth)
        {
            //System.out.println("**** Game Over ****");
            double evalScore = evaluate(state, isMaxPlayer);
            //printBoard(board);
            //System.out.println("evalScore = " + evalScore);
            //System.out.println("depth = " + depth);
            //System.out.println("********************");
            
           return new Move(null, evalScore,depth);
        }
        
        ArrayList<Move> possibleMoves = getAllPossibleMoves(state, getMinimaxPlayer(isMaxPlayer));

        // MAX play
        if(isMaxPlayer == true)
        {
            bestMove = new Move(null, Integer.MIN_VALUE, -1);
            
            for(int i=0; i < possibleMoves.size(); i++)
            {
                State copyBoard = applyMove(state, possibleMoves.get(i), getMinimaxPlayer(isMaxPlayer));
                Move move;
                if(copyBoard.isMoreTurn())
                    move = minimax(copyBoard, depth+1, maxDepth, true);
                else
                    move = minimax(copyBoard, depth+1, maxDepth, false);
                move.setLocation(possibleMoves.get(i).getLocation());
                
                if(depth==0)
                    System.out.println("#Move#" + (i+1) + ": " + possibleMoves.get(i).getLocation() + ", Score: " + move.getScore()+", Depth: " + move.getDepth());
                
                if(move.getScore() > bestMove.getScore())
                {
                    bestMove.setLocation(move.getLocation());
                    bestMove.setScore(move.getScore());
                    bestMove.setDepth(move.getDepth());
                }
                else if(move.getScore() == bestMove.getScore()){
                    if(move.getDepth()<bestMove.getDepth()){
                        bestMove.setLocation(move.getLocation());
                        bestMove.setScore(move.getScore());
                        bestMove.setDepth(move.getDepth());
                    }
                }
            }
        }
        else  // MIN player
        {
            bestMove = new Move(null, Integer.MAX_VALUE, -1);
            
            for(int i=0; i<possibleMoves.size(); i++)
            {
                State copyBoard = applyMove(state, possibleMoves.get(i), getMinimaxPlayer(isMaxPlayer));
                Move move;
                if(copyBoard.isMoreTurn())
                    move = minimax(copyBoard, depth+1, maxDepth, false);
                else
                    move = minimax(copyBoard, depth+1, maxDepth, true);
                move.setLocation(possibleMoves.get(i).getLocation());
                
                if(move.getScore() < bestMove.getScore())
                {
                    bestMove.setLocation(move.getLocation());
                    bestMove.setScore(move.getScore());
                    bestMove.setDepth(move.getDepth());
                }
            }
        }
        
        return bestMove;
    }

    /**
     * פעולת עזר למינימקס להשמת המהלך
     * @param state - הלוח
     * @param move - המהלך ליישום
     * @param currentPlayer - מספר השחקן שנרצה ליישם את המהלך בשבילו
     * @return את הלוח המעודכן עם המהלך
     */
    private State applyMove(State state, Move move, int currentPlayer)
    {
        State copy = state.duplicateState();
        return move.makeMove(move, currentPlayer, copy);
    }
    
    /**
     * פעולת עזר למינימקס להערכת הלוח 
     * @param state - הלוח שנרצה להעריך
     * @param playerB - בוליאן על מנת שנדע לאיזה שחקן אנו רוצים להעריך את הלוח 
    * @return חישוב הערכת הלוח לטובת השחקן
     */
    private double evaluate(State state,boolean playerB)
    {
        int player;
        if(playerB==true){
            player=controller.getCurrentPlayer();
            if(state.checkCopyWin(state)==player)
                return 100;
        }
        else{
            player=controller.getOpponent(controller.getCurrentPlayer());
            if(state.checkCopyWin(state)==player)
                return -100;
        }
        if(checkTie())
            return 0;
        double sum=eval1(state,player)*0.5 + eval2(state,player)*0.2 +eval3(state,player)*0.1+eval4(state)*0.3;
        return sum;
    }
    
    /**
     * פעולת עזר להערכת הלוח שמחשבת את הפרש הגומות של השחקנים
     * @param state  -הלוח
     * @param player - מספר השחקן שנרצה לטובתו
     * @return הפרש הגומות של השחקנים לטובת השחקן
     */
    private int eval1(State state, int player)
    {
        int sumForPlayer;
        int sumForOponnent;
        if(player==PLAYER_ONE){
            sumForPlayer = state.getOpponentSum();
            sumForOponnent = state.getPlayerSum();
        }
        else{
            sumForPlayer = state.getPlayerSum();
            sumForOponnent = state.getOpponentSum();
        }
        return (sumForPlayer-sumForOponnent)*8;
    }
    
    /**
     * פעולת עזר להערכת הלוח שמחשבת את הפרש מספר האבנים בין השחקנים
     * @param state  -הלוח
     * @param player - מספר השחקן שנרצה לטובתו
     * @return הפרש האבנים של השחקנים לטובת השחקן
     */
    private int eval2(State state, int player)
    {
        int sumForPlayer = state.getSumOfRocksLeft(player);
        int sumForOponnent = state.getSumOfRocksLeft(controller.getOpponent(player));
        return (sumForPlayer-sumForOponnent)*5;
//        if(sumForPlayer>sumForOponnent)
//            return 100;
//        if(sumForPlayer<sumForOponnent)
//            return -100;
//        return 0;
    }
    
    /**
     * פעולת עזר להערכת הלוח שמחשבת את הפרש מספר התורות הנוספים בין השחקנים
     * @param state  -הלוח
     * @param player - מספר השחקן שנרצה לטובתו
     * @return הפרש התורות הנוספים של השחקנים לטובת השחקן
     */
    private int eval3(State state, int player)
    {
        int sum1 = state.getSumOfMoreTurns(state, player);
        int sum2 = state.getSumOfMoreTurns(state, controller.getOpponent(player));
        return (sum1-sum2)*5;
    }
    
    /**
     * פעולת עזר להערכת הלוח שמחזירה ניקוד אם יש ללוח עוד תור
     * @param state - הלוח
     */
    private int eval4(State state)
    {
        int sum=0;
        if(state.isMoreTurn())
            sum =10;
        return (sum);

    }

    /**
     * פעולה לקבלת כל המהלכים האפשריים של השחקן
     * @param state - הלוח
     * @param player - השחקן
     * @return רשימה של כל המהלכים האפשריים
     */
     public ArrayList<Move> getAllPossibleMoves(State state, int player)
    {
        return state.getAllPossibleMoves(state,player);
    }
     
     /**
      * פעולת עזר לביצוע המשחק המחזירה את כל המקומות שצריך לעדכן
      * @param state - הלוח
      * @param move - המהלך שנרצה ליישם
      * @return רשימה  של מהלכים שנרצה לעדכן את תוכנם
      */
    private ArrayList<Move> getAllMoves(State state, Move move)
    {
        return state.getAllMoves(state,move);
    }
    
    public int getDefaultSize(){
        return currentState.getSUM();
    }

    /**
     * פעולה לקבלת האם השחקן הוא השחקן שנרצה לטובתו או השחקן היריב
     * @param maxPlayer - האם השחקן הוא שנרצה לטובתו או לא
     * @return מספר השחקן
     */
    private int getMinimaxPlayer(boolean maxPlayer)
    {
        return maxPlayer ? controller.getCurrentPlayer() : controller.getOpponent(controller.getCurrentPlayer());
    }
    
    /**
     * פעולה לבדיקת תקינות המשחק 
     * @return את הלוח שנרצה לבדוק את תקינותו
     */
    public State checkState()
    {
        State stateC = currentState.buildStateToCheck(currentState);
        currentState = stateC;
        return stateC;
    }
    
    /**
     * פעולה להדפסת כל המהלכים האפשריים
     * @param player - מספר השחקן שנרצה להדפיס את מהלכיו האפשריים
     */
     public void printAllPossibleMoves(int player)
    {
        ArrayList<Move> possibleMoves =  getAllPossibleMoves(currentState, player);
        for (int i = 0; i < possibleMoves.size(); i++)        
        {
            System.out.println(possibleMoves.get(i));
        }
    }
}
