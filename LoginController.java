import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Scanner;

public class LoginController{
    // UI Input fields and status updates from the login screen
    private JTextField userField;
    private JPasswordField passField;
    private JLabel status;
    
    // Parallel ArrayLists mapping profiles and level progress
    private ArrayList<String> userID = new ArrayList<>();
    private ArrayList<String> userPass = new ArrayList<>();
    private ArrayList<Integer> maxLevels = new ArrayList<>();
    private PCDefenderApp app;
    
    public LoginController(JTextField userField, JPasswordField passField, JLabel status, PCDefenderApp app){
        this.userField = userField;
        this.passField = passField;
        this.status = status;
        this.app = app;
        
        // sync the save-file on startup
        loadUserData();
    }
    
    //Reads "UserInfo.txt" line-by-line and splits the date into the lists
    private void loadUserData(){
        userID.clear();
        userPass.clear();
        maxLevels.clear();
        
        Scanner userIn = Prompt.getInputScannerPromptless("UserInfo.txt");
        if(userIn == null) return; //abort if txt file is not found
        
        while(userIn.hasNextLine()){
            String current = userIn.nextLine().trim();
            if(current.isEmpty()) continue; // Skip blank formatting lines
            
            // Splits a text row eg. "player1,pass123,3" around commas
            String[] tokens = current.split(",");
            if(tokens.length >= 3){
                this.userID.add(tokens[0]);       // Index match: username
                this.userPass.add(tokens[1]);     // Index match: password
                this.maxLevels.add(Integer.parseInt(tokens[2])); // Index match: max cleared level
            }
        }
        userIn.close();
    }
    

    //Verifies text entries against stored information
    public void loginUser(){
        String username = userField.getText();
        String password = new String(passField.getPassword()); // convert array char sequence to String
        
        if(username.isEmpty() || password.isEmpty()){
            this.status.setText("Please enter username and password");
            return;
        }
        
        int userIndex = this.userID.indexOf(username);
        if(userIndex != -1){
            // Evaluate if password at matched array index slot equals user input
            if(this.userPass.get(userIndex).equals(password)){
                this.status.setText("Login successful");
                this.app.setCurrentUser(username, this.maxLevels.get(userIndex)); 
                this.app.showLevelSelect(); // Transition to node (level) select screen
            }
            else{
                this.status.setText("Wrong password");
            }
        }
        else{
            this.status.setText("User not found");
        }
    }
    
    //Guarantees unique system profiles before appending new indexes
    public void registerUser(){
        String username = this.userField.getText();
        String password = new String(this.passField.getPassword());
        
        if(!username.isEmpty() && !password.isEmpty()){
            // catch duplicate username
            if(this.userID.contains(username)){
                this.status.setText("Username taken");
                return;
            }
            // add new user
            this.userID.add(username);
            this.userPass.add(password);
            this.maxLevels.add(1); // new user starts on Node 1
            
            saveAllData(); // save to text file
            this.status.setText("Register successful! Click Login.");
        }
        else{
            this.status.setText("Please enter username and password");
        }
    }
    
    //update clearance when a level is cleared
    public void updateSaveFile(String username, int netNewLevel){
        int index = this.userID.indexOf(username);
        if(index != -1){
            this.maxLevels.set(index, netNewLevel);
            saveAllData(); // Save changes to protect progression data
        }
    }
    
    //Translates local parallel lists into a structured string for file exports.
    private void saveAllData(){
        String output = "";
        for(int i = 0; i < userID.size(); i++){
            // Concatenate back into comman-separated format
            output += this.userID.get(i) + "," + this.userPass.get(i) + "," + this.maxLevels.get(i);
            // add line-breaks to separate account entries
            if(i != userID.size() - 1){
                output += "\n";
            }
        }
        // Write the compiled text onto the file
        Prompt.getPrintWriterPromptless("UserInfo.txt", output, false);
    }
    
    public ArrayList<String> getUserID(){ return this.userID; }
}