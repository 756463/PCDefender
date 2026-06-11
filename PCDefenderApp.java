import javax.swing.*;
import java.awt.*;

public class PCDefenderApp extends JFrame{
    // Screen swap state using cardlayout
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    // View panels representing different scenes of the game
    private LoginPanel loginPanel;
    private LevelSelectPanel levelSelectPanel;
    private GameBoard gameBoard;
    
    // Session states capturing active user details
    private String currentUser;
    private int maxLevelReached = 1;
    
    public PCDefenderApp(){
        setTitle("PC Defender: Cyber Defense System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // create layout and main panels
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        loginPanel = new LoginPanel(this);
        levelSelectPanel = new LevelSelectPanel(this);
        
        // add login and level select panels
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(levelSelectPanel, "LEVEL_SELECT");
        

        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN"); //force load on startup for speed
        
        pack(); 
        setLocationRelativeTo(null); // Anchor frame onto the center of the screen
        setVisible(true);
    }
    //assign score after user inputs their profile, 
    //rather than continuously reading from file
    public void setCurrentUser(String username, int maxLevel){
        this.currentUser = username;
        this.maxLevelReached = maxLevel;
    }
    
    // getters for profile information
    public String getCurrentUser(){return currentUser;}
    public int getMaxLevelReached(){return maxLevelReached;}
    
    public void showLevelSelect(){
        levelSelectPanel.refreshNodeAccess(); // refresh lock status on levels when switching to level select
        cardLayout.show(mainContainer, "LEVEL_SELECT");
        pack();
        setLocationRelativeTo(null);
    }
    
    //replace any old gameboards with a new one
    public void launchLevel(int levelNumber){
        gameBoard = new GameBoard(this, levelNumber); 
        mainContainer.add(gameBoard, "GAME_SCREEN");   
        cardLayout.show(mainContainer, "GAME_SCREEN");
        pack();
        setLocationRelativeTo(null);
        gameBoard.requestFocusInWindow(); // focus window onto the gameboard
    }
    
    //checks if player has cleared a level
    public void updatePlayerProgress(int levelCleared){
        if(levelCleared >= maxLevelReached && maxLevelReached < 5){
            this.maxLevelReached = levelCleared + 1;
            // updates stored player data
            loginPanel.getController().updateSaveFile(currentUser, maxLevelReached);
        }
    }
    
    //Main entry method spawning the application cycle thread.
    public static void main(String[] args){
        // Thread Safety: Passes initiation loops over onto Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new PCDefenderApp());
    }
}