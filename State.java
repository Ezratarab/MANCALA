package ver3;

import java.util.ArrayList;

/**
 * State of the Mancala Game
 * @author Ezra Tarab | 03/05/2023
 */
public class State
{
    // Attributes תכונות
    private int logicBoard[][];
    private int COLS = 8;
    private int ROWS = 2;
    private int RESET_ROCKS = 4;
    private int SUM = RESET_ROCKS*ROWS*COLS;
    private int playerSum;
    private int opponentSum;
    private boolean moreTurn;

    /**
     * פעולה בונה היוצרת את הלוח
     */
    public State()
    {
        logicBoard = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                logicBoard[row][col] = RESET_ROCKS;
            }
        }
        moreTurn = false;
    }
    
    /**
     * פעולה היוצרת את הלוח לבדיקת תקינות המשחק
     * @param state - הלוח שנרצה לשנות
     * @return לוח עדכני שנרצה לבדוק את תקינות המשחק עליו
     */
    public State buildStateToCheck(State state)
    {
        int[][] l = new int[ROWS][COLS];
        l[0][0]=1;
        l[1][COLS-1]=3;
        state.logicBoard=l;
        state.opponentSum=31;
        state.playerSum=29;
        
        return state;
    }
    public void setPlayerSum(int playerSum)
    {
        this.playerSum = playerSum;
    }

    public void setOpponentSum(int opponentSum)
    {
        if(opponentSum>=0&&opponentSum<(SUM))
            this.opponentSum = opponentSum;
    }


    public int getValue(int row, int col)
    {
       return logicBoard[row][col];
    }

    public void setValue(int row, int col, int newSum)
    {
        logicBoard[row][col]=newSum;
    }

    public int getPlayerSum()
    {
        return playerSum;
    }

    public int getOpponentSum()
    {
        return opponentSum;
    }
    public int checkWin()
    {
        int winner =  checkCopyWin(this);
        return winner;
    }
    
    /**
     * פעולת עזר לבדיקת ניצחון
     * @param state - הלוח שנרצה לבדוק את ניצחון המשחק
     * @return את מספר השחקן המנצח אם מישהו ניצח אם יש תיקו מחזיר את מספר התיקו ואם אין ניצחון מחזיר אפס
     */
    public int checkCopyWin(State state)
    {
       int noRocksPlayer = noRocksLeft(state);
       if(noRocksPlayer!=0){
            setEndBoard(state,noRocksPlayer);
            if(state.getPlayerSum()>state.getOpponentSum())
                noRocksPlayer = Model.PLAYER_TWO;
            else if(state.getPlayerSum()<state.getOpponentSum())
                noRocksPlayer = Model.PLAYER_ONE;
            else
            {
                if(checkTie(state))
                    return Model.TIE_NUMBER;
            } 
       }
       return noRocksPlayer;
    }
    
    /**
     * פעולה הבודקת אם יש תיקו
     * @param state - הלוח שנרצה לבדוק עליו אם יש תיקו
     * @return אם יש תיקו או לא
     */
    public boolean checkTie(State state)
    {
        return (state.getOpponentSum() == state.getPlayerSum() && (state.getPlayerSum()+state.getOpponentSum()==state.SUM));
    }
    @Override
    public String toString()
    {
        String str = "";
        str+="\nsum for opponent: "+getOpponentSum()+"\n";
        str+="sum for you: "+getPlayerSum()+"\n";
        for (int row= 0; row < ROWS; row++)        
        {
            for (int col= 0; col < COLS; col++)            
            {
                str+=" "+logicBoard[row][col];
            }
            str+="\n";
        }
        
        return str;
    }

    public int[][] getLogicBoard()
    {
        return logicBoard;
    }
    
    /**
     * פעולה הבונה רשימה של כל המהלכים האפשריים של השחקן
     * @param state - הלוח
     * @param player - השחקן שנרצה את כל מהלכיו האפשריים
     * @return רשימה של מהלכים אפשריים של השחקן
     */
    public ArrayList<Move> getAllPossibleMoves(State state, int player)
    {
        ArrayList<Move> openMoves = new ArrayList();
        for (int i = 0; i < state.COLS; i++)
        {
            if (state.getValue(player - 1, i) != 0)
            {
                openMoves.add(new Move(new Location(player - 1, i)));
            }
        }
        return openMoves;
    }
    
    /**
     * פעולת עזר הבונה רשימה של כל המהלכים שצריך לשנות את ערכם
     * @param state - הלוח
     * @param move - המהלך שנרצה ליישם
     * @return רשימה של כל המהלכים שצריך לשנות את ערכם
     */
    public static ArrayList<Move> getAllMoves(State state, Move move)
    {
        int sum = state.getValue(move.getLocation().getRow(), move.getLocation().getCol())+1;
        ArrayList<Move> possibleMoves = new ArrayList();
        int startCol=move.getLocation().getCol();
        while (sum != 0)
        {
            if (move.getLocation().getRow() == 0)
            {
                for (int j = startCol; j >=0 && sum > 0; j--,sum--)
                {
                    possibleMoves.add(new Move(new Location(move.getLocation().getRow(),j)));
                }
                if (sum != 0)
                {
                    possibleMoves.add(new Move(new Location(-1,-1)));
                    sum--;
                }
                for (int i = 0; i < state.COLS && sum > 0; i++,sum--)
                {
                    possibleMoves.add(new Move(new Location(1, i)));
                }
                startCol=0;
                
            } else if(move.getLocation().getRow()==1)
            {
                for (int i = startCol; i < state.COLS&& sum > 0; i++,sum--)
                {
                    possibleMoves.add(new Move(new Location(1, i)));
                }
                if (sum != 0)
                {
                    possibleMoves.add(new Move(new Location(-1,-1)));
                    sum--;
                }
                for (int j = state.COLS-1; j >=0 && sum > 0; j--,sum--)
                {
                    possibleMoves.add(new Move(new Location(0, j)));
                }
                 startCol=0;
            }
        }
        possibleMoves.remove(0);
        return possibleMoves;
    }

    public int getSUM()
    {
        return SUM;
    }

    public boolean isMoreTurn()
    {
        return moreTurn;
    }

    public void setMoreTurn(boolean moreTurn)
    {
        this.moreTurn = moreTurn;
    }
    
    /**
     * פעולה הבודקת לאיזה שחקן נגמרו כל האבנים
     * @param state - הלוח
     * @return את מספר השחקן שנגמרו לו כל האבנים
     */
    private int noRocksLeft(State state){
        int sum1=0,sum2=0;
        for (int col= 0; col < COLS; col++)        
        {
            if(state.getValue(Model.PLAYER_ONE-1, col)==0)
                sum1++;
            if(state.getValue(Model.PLAYER_TWO-1, col) == 0)
                sum2++;
        }
        if(sum1 == COLS)
            return Model.PLAYER_ONE;
        if(sum2 == COLS)
            return Model.PLAYER_TWO;
        return 0;
    }
    
    /**
     * פעולה המסדרת את הלוח למקרה שנגמר המשחק
     * @param state - הלוח
     * @param playerWithNoRocks - מספר השחקן שנגמרו לו כל האבנים
     */
    private void setEndBoard(State state,int playerWithNoRocks)
    {
        int row;
        if(playerWithNoRocks == Model.PLAYER_ONE)
            row = Model.PLAYER_TWO-1;
        else
            row = Model.PLAYER_ONE-1;
        int sum=0;
        for (int col= 0; col < COLS; col++)        
        {
            if(state.getValue(row, col)!=0){
                sum += state.getValue(row, col);
                state.setValue(row, col, 0);
            }
        }
        if(row == 0)
            state.setOpponentSum(sum + state.getOpponentSum());
        else
            state.setPlayerSum(state.getPlayerSum() + sum);
    }
    
    /**
     * פעולה הסוכמת כמה אבנים נשארו לשחקן
     * @param player - השחקן
     * @return את מספר האבנים שנשארו לשחקן
     */
    public int getSumOfRocksLeft(int player){
        int sum=0;
        for (int col= 0; col < COLS; col++)        
        {
            sum+=logicBoard[player-1][col];
        }
        return sum;
    }
    
    /**
     * פעולה הסופרת את מספר הפעולות הנוספים שיש לשחקן
     * @param state - הלוח
     * @param player - השחקן שנצרה לבדוק כמה תורות נוספים יש לו
     * @return את מספר התורות הנוספים שיש לשחקן
     */
    public int getSumOfMoreTurns(State state,int player){
        int sum=0;
        if (player == Model.PLAYER_TWO)
        {
            for (int col = 0; col < COLS; col++)
            {
                if (state.getValue(player - 1, col) + col == COLS)
                {
                    sum++;
                }
            }
        }
        else{
            for (int col = COLS-1; col >=0; col--)
            {
                if (state.getValue(player - 1, col) - (col+1) == 0)
                {
                    sum++;
                }
            }
        }
        return sum;
    }
    
    /**
     * פעולה המשכפלת את הלוח
     * @return את השכפול של הלוח
     */
    public State duplicateState(){
        State copyState = new State();
        for (int row = 0; row < ROWS; row++)        
        {
            for (int col = 0; col < COLS; col++)            
            {
                copyState.setValue(row, col, this.getValue(row, col));
            }
        }
        copyState.setPlayerSum(this.getPlayerSum());
        copyState.setOpponentSum(this.getOpponentSum());
        return copyState;
    }
}
