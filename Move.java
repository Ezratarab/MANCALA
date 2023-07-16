package ver3;

import java.util.ArrayList;

/**
 * Move of the Mancala Game
 * @author Ezra Tarab | 03/05/2023
 */
public class Move
{
    // Attributes תכונות
    private Location location;
    private double score;
    private int depth;
    // Methoods פעולות

    public Move(Location location, double score, int depth)
    {
        this.location = location;
        this.score = score;
        this.depth = depth;
    }

    public Move(Location location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "Move{" + "location=" + location + ", score=" + score + ", depth=" + depth + '}';
    }

    public Location getLocation()
    {
        return location;
    }

    public double getScore()
    {
        return score;
    }

    public int getDepth()
    {
        return depth;
    }

    /**
     * פונקציה היוצרת את המהלך על הלוח
     * @param move - המהלך שנרצה
     * @param playerSign - מספר השחקן
     * @param state - הלוח שנרצה ליישם עליו את המהלך
     * @return את הלוח המעודכן עם המהלך
     */
    public State makeMove(Move move, int playerSign, State state){
        int sumEnd=10;//random number
        int col=move.getLocation().getCol();
        int row=move.getLocation().getRow();
        int sum=state.getValue(row,col);
        if (sum != 0)
        {
            ArrayList<Move> allMoves = state.getAllMoves(state, move);
            Move lastMove = allMoves.get(allMoves.size() - 1);
            state.setValue(row, col, 0);
            Move temp;
            int lastMoveRow = move.getLocation().getRow();
            int lastMoveRow2;
            for (int i = 0; i < allMoves.size()-1 && sum != 0; i++, sum--)
            {
                if (i > 0)
                {
                    lastMoveRow = allMoves.get(i - 1).getLocation().getRow();
                }
                temp = allMoves.get(i);
                
                if (temp.getLocation().getRow() != -1 && temp.getLocation().getCol() != -1)
                {
                    int tempSum = state.getValue(temp.getLocation().getRow(), temp.getLocation().getCol());
                    state.setValue(temp.getLocation().getRow(), temp.getLocation().getCol(), tempSum + 1);
                } 
                //למקרה שצריך לעבור בגומה הגדולה
                else
                {
                    if (lastMoveRow == 0)
                    {
                        state.setOpponentSum(state.getOpponentSum() + 1);
                    } else if (lastMoveRow == 1)
                    {
                        state.setPlayerSum(state.getPlayerSum() + 1);
                    }
                }
            }
            if (lastMove.getLocation().getRow() != -1 && lastMove.getLocation().getCol() != -1)
            {
                sumEnd = state.getValue(lastMove.getLocation().getRow(), lastMove.getLocation().getCol());
                state.setValue(lastMove.getLocation().getRow(), lastMove.getLocation().getCol(), sumEnd + 1);
            } 
            //למקרה שהאבן האחרונה נופלת בגומה הגדולה
            else
            {
                if (allMoves.size() > 2)
                {
                    lastMoveRow2 = allMoves.get(allMoves.size() - 2).getLocation().getRow();
                } 
                //למקרה שלחצנו על גומה שיש בה אבן אחת והיא צמודה לגומה הגדולה
                else
                {
                    lastMoveRow2 = move.getLocation().getRow();
                }
                if (lastMoveRow2 == 0)
                {
                    state.setOpponentSum(state.getOpponentSum() + 1);
                } else
                {
                    state.setPlayerSum(state.getPlayerSum() + 1);
                }
                state.setMoreTurn(true);
            }
            
            //למקרה שהאבן האחרונה נופלת באותה שורה ונופלת בגומה שאין בה אבנים
            if (lastMove.getLocation().getRow() == move.getLocation().getRow() && sumEnd == 0)
            {
                if (lastMove.getLocation().getRow() == 0)
                {
                    //למקרה שיש בגומה המקבילה אבנים
                    if (state.getValue(1, lastMove.getLocation().getCol()) != 0)
                    {
                        state.setOpponentSum(state.getOpponentSum() + state.getValue(0, lastMove.getLocation().getCol()) + state.getValue(1, lastMove.getLocation().getCol()));
                        state.setValue(0, lastMove.getLocation().getCol(), 0);
                        state.setValue(1, lastMove.getLocation().getCol(), 0);
                    }
                } else
                {
                    if (state.getValue(0, lastMove.getLocation().getCol()) != 0)
                    {
                        state.setPlayerSum(state.getPlayerSum() + state.getValue(0, lastMove.getLocation().getCol()) + state.getValue(1, lastMove.getLocation().getCol()));
                        state.setValue(0, lastMove.getLocation().getCol(), 0);
                        state.setValue(1, lastMove.getLocation().getCol(), 0);
                    }
                }
            }
        }
        //אם לחצנו על גומה שאין בה אבנים ניתן לשחקן עוד תור כי הוא לא עשה מהלך
        else
            state.setMoreTurn(true);
        return state;
    }
    
    public void setLocation(Location location)
    {
        this.location = location;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }
    
}
