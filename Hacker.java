import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Hacker extends Enemy{
    // timer tracking time between EMP blasts
    private int empCooldown = 0;
    // Shared static sprite
    private static Image sprite;
    
    public Hacker(double startX, int lane){
        // Sets boss stats: 1200 high HP, very slow 0.3 speed, 3 generic damage
        super(1200, 0.3, 3, startX, lane);
        
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/Hacker.png")).getImage();
            } catch(Exception e){
                System.out.println("Could not load Hacker.png");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        // Slowly advance left across the board
        step(); 
        // Tick up the EMP timer
        empCooldown++;
        // At 120 frames fire EMP
        if(empCooldown >= 120){ 
            for(int c = 0; c < GameBoard.COLS; c++){
                // Scan the entire row to locate defenses
                if(board.getPlants()[lane][c] != null){
                    // Deal 60 damage to the first unit, ignoring distance
                    board.getPlants()[lane][c].takeDamage(60); 
                    break; // Stop scanning after target hit
                }
            }
            empCooldown = 0; // Reset countdown for the next blast cycle
        }
    }
    
    @Override
    public void draw(Graphics g){
        // Calculate vertical position to keep the enemy centered in its row
        int yPos = lane * GameBoard.CELL_HEIGHT + 15;
        if(sprite != null){
            g.drawImage(sprite,(int) x, yPos, 60, 70, null);
        }
        else{
            // Vector fallback rendering a black terminal box with neon green code text
            g.setColor(Color.BLACK);
            g.fillRect((int) x, yPos, 55, 75); 
            g.setColor(Color.GREEN);
            g.drawRect((int) x, yPos, 55, 75);
            g.setFont(new Font("Monospaced", Font.BOLD, 12));
            g.drawString("0x90",(int) x + 12, yPos + 40); // "0x90" easter egg: assembly language code for a NOP (No Operation)
        }
    }
}