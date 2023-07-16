package ver3;

/**
 * Controller of the Mancala Game
 * @author Ezra Tarab | 03/05/2023
 */
public class Controller
{
    // Fields - תכונות
    private Model model;  // מודל
    private View view;    // תצוגה
    private int currentPlayer; // סימן השחקן הנוכחי זה שתורו לשחק כעת

    /**
     * יצירת המשחק פעם יחידה בלבד
     */
    public Controller()
    {
        // Create the game Model & View
        model = new Model(this);
        view = new View(this);
    }
    
     /**
     * New Game setup.
     * called every time new game started
     */
    public void startNewGame()
    {
        currentPlayer = Model.PLAYER_TWO; 
        
        model.setup();  // לוח משחק לוגי למצב התחלת משחק
        view.setup();   // לוח משחק תצוגה למצב התחלת משחק
        
        System.out.println(model);
    }
    
    
    /** btnNewGame button
     * btnNewGame פעולה זו מזומנת על ידי התצוגה בעת לחיצה על כפתור
     * לצורך ביצוע התחלת משחק חדש
     */
    public void newGameButtonPressed()
    {
        System.out.println("newGameButtonPressed");
        startNewGame();
    }
     
    /**
     * btnAIMoveפעולה זו מזומנת על ידי התצוגה בעת לחיצה על כפתור 
     * לצורך ביצוע מהלך של שחקן ממוחשב
     */
    public void aiMoveButtonPressed()
    {
        System.out.println("aiMoveButtonPressed");
 
      	// צריך לבקש מהמודל מהלך ממוחשב אקראי או ראשון פנוי או בינה מלאכותית 
        Move move = model.getAiMove();
        
        // update view - שים בכפתור הלוח במיקום שהתקבל את סימן השחקן
        //view.updateBoardButton(updatedBoard);
        
        //checkGameStatus();
        boardButtonPressed(move);
    }
    
    /**
     * פעולה זו מזומנת על ידי התצוגה בעת לחיצה על אחד מכפתורי לוח המשחק
     * לצורך ביצוע מהלך של שחקן אנושי
     * @param move - המהלך שנרצה ליישם
     */
    public void boardButtonPressed(Move move)
    {
        System.out.println("boardButtonPressed move="+move);
        
        State updatedBoard= model.makeMove(move, currentPlayer);
        System.out.println(updatedBoard);
        
        // update view - שים בכפתור הלוח במיקום שהתקבל את סימן השחקן
        view.updateBoardButton(updatedBoard);
        System.out.println("\ncurrent state more turn: " +updatedBoard.isMoreTurn()+"\n");
        if(updatedBoard.isMoreTurn()==true){
            getMoreTurn();
            updatedBoard.setMoreTurn(false);
        }
        checkGameStatus();
    }
    
 
    /**
     *   הפעולה בודקת את מצב המשחק
    * האם המשחק ניגמר בניצחון או תיקו
    * אם המשחק לא ניגמר אז מבצעים החלפת תור שחקן
     */
    private void checkGameStatus()
    {
        System.out.print(">> checkGameStatus: ");

        //1. נבדוק האם יש ניצחון לשחקן הנוכחי זה ששיחק כעת
        // =============================================
        int winner = model.checkWin();
        if (winner == currentPlayer)
        {
            String msg = "Game Over - " + winner + " Win!";
            System.out.println(msg);
            view.gameOver(msg, winner);
            return;
            
        }
        if (winner == getOpponent(currentPlayer))
        {
            String msg = "Game Over - " + winner + " Win!";
            System.out.println(msg);
            view.gameOver(msg, winner);
            return;
            
        }

        //2. נבדוק האם יש תיקו כלומר נגמר המשחק ואף אחד לא ניצח
        // ====================================================
        if (model.checkTie() == true)
        {
            String msg = "Game Over - Tie!";
            System.out.println(msg);
            view.gameOver(msg, 0);
            return;
        }
        

        //3. אין ניצחון ואין תיקו מחליפים תור וממשיכים לשחק
        // ====================================================
        switchTurn();
    }
    
    /**
     * פונקציה להחלפת תורות שחקנים
     */
    private void switchTurn()
    {
        System.out.println("switch players & continue to play...");
        
        // החלפת סימן שחקן תור מתחלף
        if (currentPlayer == Model.PLAYER_TWO){
            currentPlayer = Model.PLAYER_ONE;
            view.setLableMsg("His Turn");
        }
        else{
            currentPlayer = Model.PLAYER_TWO;
            view.setLableMsg("Your Turn");
        }
    }
    /**
     * פונקציה לקבלת השחקן שעכשיו תורו
     * @return את מספר השחקן אחד או שתיים
     */
      public int getCurrentPlayer()
    {
        return currentPlayer;
    }
      /**
       * פונקציה לקבלת השחקן היריב
       * @param player - מספר השחקן שנרצה לקבל את יריבו
       * @return את יריב השחקן
       */
      public char getOpponent(int player){
        if (player==Model.PLAYER_ONE)
                return Model.PLAYER_TWO;
        return Model.PLAYER_ONE;
    }
    /**
     * על מנת לעדכן את הגומה הראשית של השחקן הנגדי
     * @param sum1 - המספר המעודכן
     */
    public void updateSum1(int sum1){
        view.updateSum1(sum1);
    }
    /**
     * על מנת לעדכן את הגומה הראשית של השחקן 
     * @param sum2 - המספר המעודכן
     */
    public void updateSum2(int sum2){
        view.updateSum2(sum2);
    }
    
    /**
     * פונקציה המעדכנת את הלוח לקבלת עוד תור
     */
    public void getMoreTurn(){
        switchTurn();
        view.moreTurn();
    }

    /** check State button
     *  פעולה זו מזומנת על ידי התצוגה בעת לחיצת על כפתור העליון לצורך בדיקה של תקינות המשחק
     */
    public void checkStateButtonPressed(){
       State s = model.checkState();
       view.updateBoardButton(s);
    }
    
    /** possible moves button
     * פונקציה זו מזומנת על ידי התצוגה בעת לחיצה על הכפתור העליון של כל המהלכים האפשריים
     * מדפיסה את  כל המהלכים האפשריים
     */
    public void possibleMovesButtonPressed(){
        String player;
        if(currentPlayer==Model.PLAYER_ONE)
            player = "opponent player";
        else
            player = "you";
        System.out.println("\npossible moves for "+player+": \n");
        model.printAllPossibleMoves(currentPlayer);
    }
    
    /**
     * main function 
     */
    // ===============================================	
    public static void main(String[] args)
    {
        Controller game = new Controller();
        game.startNewGame();
    }
    // ===============================================   
}



