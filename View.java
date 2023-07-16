package ver3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * View - MANCALA Game GUI 
 * @author Ezra Tarab
 */
public class View 
{
    // קבועים
    public static final String WIN_TITLE = "MANCALA GAME";
    public static final Font FONT1 = new Font(null, Font.BOLD, 60);  // פונט לכפתורים
    public static final Font FONT2 = new Font(null, Font.BOLD, 16);  // פונט לכפתורים
    private static int ROWS=2;  // מספר השורות במטריצה
    private static int COLS=8;  // מספר העמודות במטריצה
    private static int DEFAULT_SUM = 4;
    private static final Color ONE_COLOR = Color.YELLOW; //פונט לשחקן הראשון
    private static final Color TWO_COLOR = Color.GREEN;  //פונא לשחקן השני

    // משתנים 
    private JFrame mancala;
    private JButton btnNewGame, btnAIMove,btnCheckState,btnPossibleMoves;
    private JLabel lblMsg;       // תוית להצגת תור מי לשחק
    private JButton[][] btnMat;  // מטריצת הכפתורים
    private Controller controller;
    private JLabel sum1;
    private JLabel sum2; 
    
    public View(Controller controller)
    {
        // Save the controller 
        this.controller = controller;
        
        // Create GUI...
        createGUI();
    }
    
    public void setup()
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                btnMat[row][col].setText(DEFAULT_SUM+"");
                btnMat[row][col].setEnabled(true);
                btnMat[row][col].setBackground(Color.WHITE);
            }
        }
        //btnNewGame.setEnabled(false);
        btnAIMove.setEnabled(true);
        lblMsg.setText("Your Turn!");
        lblMsg.setBackground(TWO_COLOR);
        sum1.setBackground(Color.WHITE);
        sum1.setText("0");
        sum1.setHorizontalAlignment(SwingConstants.CENTER);
        sum2.setBackground(Color.WHITE);
        sum2.setText("0");
        sum2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        setBoardButtonsPlayerEnabled(controller.getOpponent(controller.getCurrentPlayer()), false);
        
    }
    
    // פעולה היוצרת את הממשק הגרפי של המשחק
    private void createGUI()
    {
        // יצירת החלון למחשבון
        mancala = new JFrame(WIN_TITLE);
        mancala.setSize(1200, 500);
        mancala.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        // יצירת פאנל עליון לכפתורי ניהול המשחק
        JPanel pnlTop = new JPanel();
        pnlTop.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // כפתור להתחלת משחק חדש
        btnNewGame = new JButton("New Game");
        btnNewGame.setFocusable(false);
        btnNewGame.setForeground(Color.BLUE);
        btnNewGame.setFont(FONT2);
        btnNewGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // TODO: tell to controller that newGame button, pressed!
                controller.newGameButtonPressed();
            }
        });
        pnlTop.add(btnNewGame);
        
        btnCheckState = new JButton("Check State");
        btnCheckState.setFocusable(false);
        btnCheckState.setForeground(Color.BLUE);
        btnCheckState.setFont(FONT2);
        btnCheckState.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // TODO: tell to controller that newGame button, pressed!
                controller.checkStateButtonPressed();
            }
        });
        pnlTop.add(btnCheckState);
        
        btnPossibleMoves = new JButton("Possible Moves");
        btnPossibleMoves.setFocusable(false);
        btnPossibleMoves.setForeground(Color.BLUE);
        btnPossibleMoves.setFont(FONT2);
        btnPossibleMoves.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // TODO: tell to controller that newGame button, pressed!
                controller.possibleMovesButtonPressed();
            }
        });
        pnlTop.add(btnPossibleMoves);

        // כפתור לשחקן ממוחשב
        btnAIMove = new JButton("AI Move");
        btnAIMove.setFocusable(false);
        btnAIMove.setForeground(Color.RED);
        btnAIMove.setFont(FONT2);
        btnAIMove.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // TODO: tell to controller that btnAI button, pressed!
                controller.aiMoveButtonPressed();
            }
        });
        pnlTop.add(btnAIMove);

        // יצירת פאנל לכפתורים
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new GridLayout(ROWS, COLS));

        // מערך הכפתורים של המחשבון
        btnMat = new JButton[ROWS][COLS];

        // יצירת כל כפתורי המחשבון קביעת הפונט שלהם והוספתם לחלון על פי גריד שנקבע
        // לולאה שעוברת על כל השורות במטריצה
        for (int row = 0; row < ROWS; row++)
        {
            // לולאה שעוברת על כל העמודות
            for (int col = 0; col < COLS; col++)
            {
                // יצירת כפתור בלוח המשחק
                btnMat[row][col] = new JButton(" "); 
                btnMat[row][col].setFont(FONT1);
                btnMat[row][col].setFocusable(false);
                btnMat[row][col].setActionCommand(row + "," + col); // save indexs (row,col)
                btnMat[row][col].setForeground(Color.BLUE);
                btnMat[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                
                // הוספת מאזין לאירוע לחיצה על הכפתור
                btnMat[row][col].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // Tell to Controller that Board Button was pressed!
                        JButton btn = (JButton) e.getSource();
                        String indexs = btn.getActionCommand(); // get indexes (row,col)
                        int row = Integer.parseInt(indexs.substring(0, indexs.indexOf(',')));
                        int col = Integer.parseInt(indexs.substring(indexs.indexOf(',')+1));
                        controller.boardButtonPressed(new Move(new Location(row,col)));
                    }
                });

                // הוספת הכפתור לגריד שבפנאל
                pnlButtons.add(btnMat[row][col]);
            }
        }

        lblMsg = new JLabel("?");
        lblMsg.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        lblMsg.setOpaque(true);
        lblMsg.setFont(FONT2);
        
        sum1 = new JLabel(" ");
        sum1.setBorder(BorderFactory.createEmptyBorder(2, 100, 2, 2));
        sum1.setOpaque(true);
        sum1.setFont(FONT1);
        
        sum2 = new JLabel(" ");
        sum2.setBorder(BorderFactory.createEmptyBorder(2, 100, 2, 2));
        sum2.setOpaque(true);
        sum2.setFont(FONT1);

        // הוספת כפתור לתחילת משחק חדש לצפון החלון
        mancala.add(pnlTop, BorderLayout.NORTH);

        // הוספת הפאנל למרכז החלון
        mancala.add(pnlButtons, BorderLayout.CENTER);

        // הוספת התוית לדרום החלון
        mancala.add(lblMsg, BorderLayout.SOUTH);
        mancala.add(sum1,BorderLayout.WEST);
        mancala.add(sum2,BorderLayout.EAST);

        // מרכז החלון
        mancala.setLocationRelativeTo(null);
        
        // מציג את החלון על המסך
        mancala.setVisible(true);
        mancala.setIconImage(new ImageIcon("C:\\Users\\Ezra Tarab\\Documents\\NetBeansProjects\\Mancala\\src\\games assets\\iconmancala.png").getImage());
    }

    public void updateBoardButton(State state)
    {
        int updatedBoard[][] = state.getLogicBoard();
        for (int row= 0; row < ROWS; row++)        
        {
            for (int col= 0; col < COLS; col++)            
            {
                btnMat[row][col].setText(""+updatedBoard[row][col]);
                if(updatedBoard[row][col]==0)
                    btnMat[row][col].setEnabled(false);
                else
                    btnMat[row][col].setEnabled(true);
            }
        }
        sum1.setText(state.getOpponentSum()+"");
        sum2.setText(state.getPlayerSum()+"");
        if(controller.getCurrentPlayer()==1){
            lblMsg.setBackground(ONE_COLOR);
        }
        else{
            lblMsg.setBackground(TWO_COLOR);
        }
        setBoardButtonsPlayerEnabled(controller.getCurrentPlayer(), false);
        setBoardButtonsPlayerEnabled(controller.getOpponent(controller.getCurrentPlayer()), true);
    }

    public void gameOver(String msg, int player)
    {
        lblMsg.setText(msg);
        setBoardButtonsEnabled(false);
        btnNewGame.setEnabled(true);
        btnAIMove.setEnabled(false);
        for (int row= 0; row < ROWS; row++)        
        {
            for (int col= 0; col < COLS; col++)            
            {
                btnMat[row][col].setText("" + 0);
            }
        }
    }
    
    // פעולה לנעילה או פתיחה של כל כפתורי הלוח על פי הפרמטר שמתקבל
    // status=true - נעילה
    // status=false - פתיחה
    private void setBoardButtonsEnabled(boolean status)
    {
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS; col++)
            {
                btnMat[row][col].setEnabled(status);
            }
        }
    }
    private void setBoardButtonsPlayerEnabled(int player,boolean status)
    {
          for (int col = 0; col < COLS; col++)
            {
                btnMat[player-1][col].setEnabled(status);
            }
    }

    public void setLableMsg(String string)
    {
        lblMsg.setText(string);
    }

    public void updateSum1(int sum){
        sum1.setText(sum+"");
    }
    public void updateSum2(int sum){
        sum2.setText(sum+"");
    }
     
    public void moreTurn(){
        setBoardButtonsPlayerEnabled(controller.getCurrentPlayer(), false);
        setBoardButtonsPlayerEnabled(controller.getOpponent(controller.getCurrentPlayer()), true);
    }
}

