package ver3;

/**
 * Location defines the location of the Move Class
 * @author Ezra Tarab | 03/05/2023
 */
public class Location
{
    // Attributes תכונות
    private int row;
    private int col;
    // Methoods פעולות

    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    @Override
    public String toString()
    {
        return "Location{" + "row=" + row + ", col=" + col + '}';
    }
    
}
