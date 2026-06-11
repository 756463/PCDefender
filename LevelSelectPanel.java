import javax.swing.*;
import java.awt.*;

public class LevelSelectPanel extends JPanel{
    private PCDefenderApp app;
    private JButton[] levelButtons;
    private JLabel bannerText;
    
    public LevelSelectPanel(PCDefenderApp app){
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // title setup
        bannerText = new JLabel("SECURE THE NETWORK: SELECT AN OPERATIONAL NODE", SwingConstants.CENTER);
        bannerText.setFont(new Font("Monospaced", Font.BOLD, 18));
        bannerText.setForeground(Color.GREEN);
        bannerText.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(bannerText, BorderLayout.NORTH);
        
        // Single row layout grid holding 5 level select choices side-by-side
        JPanel buttonGrid = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonGrid.setBackground(Color.BLACK);
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(20, 40, 60, 40));
        
        levelButtons = new JButton[5];
        for(int i = 0; i < 5; i++){
            final int chosenLevel = i + 1; // offset by 1 (0-4 becomes 1-5 for levels)
            levelButtons[i] = new JButton("NODE 0" + chosenLevel);
            levelButtons[i].setFont(new Font("Monospaced", Font.BOLD, 14));
            levelButtons[i].setFocusable(false);
            
            //  lambda listener: passes the clicked level number straight to the central manager
            levelButtons[i].addActionListener(e -> app.launchLevel(chosenLevel));
            buttonGrid.add(levelButtons[i]);
        }
        
        add(buttonGrid, BorderLayout.CENTER);
    }
    
    //Toggles lock and color states based on player level progression
    public void refreshNodeAccess(){
        int clearance = app.getMaxLevelReached();
        // Dynamically updates status title text to show player names and high scores
        bannerText.setText("OPERATOR: " + app.getCurrentUser().toUpperCase() + " | MAX CLEARANCE: NODE 0" + clearance);
        
        for(int i = 0; i < 5; i++){
            // level unlocks
            if(i + 1 <= clearance){
                levelButtons[i].setEnabled(true);
                levelButtons[i].setBackground(Color.DARK_GRAY);
                levelButtons[i].setForeground(Color.GREEN);
            }
            // level is locked
            else{
                levelButtons[i].setEnabled(false);
                levelButtons[i].setBackground(new Color(40, 40, 40));
                levelButtons[i].setForeground(Color.RED);
            }
        }
    }
}