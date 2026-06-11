import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel{
    // Sub-panels for visual hierarchy and input grouping
    private JPanel loginPanel = new JPanel();
    private JPanel userPanel = new JPanel();
    private JPanel passPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    
    // UI components for branding, credential entry, and response feedback
    private JLabel title = new JLabel("PC DEFENDER!", SwingConstants.CENTER);
    private JLabel username = new JLabel("Username:");
    private JLabel password = new JLabel("Password:");
    private JTextField userField = new JTextField("Enter username", 10);
    private JPasswordField passField = new JPasswordField(10);
    private JLabel status = new JLabel("Login or Register", SwingConstants.CENTER);
    private LoginController controller;

    public LoginPanel(PCDefenderApp app){
        this.controller = new LoginController(userField, passField, status, app);
        setLayout(new GridBagLayout());
        
        // Layout Configurations: align fields and gaps
        loginPanel.setLayout(new GridLayout(5, 1, 0, 11));
        userPanel.setLayout(new GridLayout(1, 2, -112, 0));
        passPanel.setLayout(new GridLayout(1, 2, -112, 0));
        buttonPanel.setLayout(new GridLayout(1, 2, 22, 0));
        
        // Fixed Dimensions: Standardize sizes across all panels
        loginPanel.setPreferredSize(new Dimension(300, 225));
        userPanel.setPreferredSize(new Dimension(300, 45));
        passPanel.setPreferredSize(new Dimension(300, 45));
        buttonPanel.setPreferredSize(new Dimension(300, 45));
        
        //add the components to the main panels
        title.setFont(new Font("Plain", Font.BOLD, 30));
        title.setForeground(Color.RED);
        
        username.setForeground(Color.GREEN);
        password.setForeground(Color.GREEN);
        userField.setBackground(Color.BLACK);
        passField.setBackground(Color.BLACK);
        userField.setForeground(Color.GREEN);
        passField.setForeground(Color.GREEN);
        
        userPanel.add(username);
        userPanel.add(userField);
        passPanel.add(password);
        passPanel.add(passField);
        
        loginPanel.add(title);
        loginPanel.add(userPanel);
        loginPanel.add(passPanel);
        
        // login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.GREEN);
        //register button
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.GREEN);
        
        //add the buttons and status to the panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        loginPanel.add(buttonPanel);
        
        status.setForeground(Color.GREEN);
        loginPanel.add(status);
        
        // black theme
        setBackground(Color.BLACK);
        loginPanel.setBackground(Color.BLACK);
        userPanel.setBackground(Color.BLACK);
        passPanel.setBackground(Color.BLACK);
        buttonPanel.setBackground(Color.BLACK);
        add(loginPanel);
        
        //add action listener to the buttons
        //when login button is clicked , call "loginUser()"
        loginButton.addActionListener(e -> controller.loginUser());
        
        //when register button is clicked, call "registerUser()"
        registerButton.addActionListener(e -> controller.registerUser());
    }
    
    //Connects information to other classes
    public LoginController getController(){
        return this.controller;
    }
}