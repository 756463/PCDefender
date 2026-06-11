import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Popup extends Enemy{
    // Shared static sprite
    private static Image sprite;
    
    public Popup(double startX, int lane){
        // Sets health, movement speed, damage, and spawn origins in parent Enemy class
        super(100, 1.2, 1, startX, lane);
        
        // load custom image from file
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/Popup.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load Popup.png");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        // translate x coordinate into a grid coordinate
        int col = (int)(x / GameBoard.CELL_WIDTH);
        
        // Collision Detection: Checks if enemy collides with plant
        if(col >= 0 && col < GameBoard.COLS && board.getPlants()[lane][col] != null){
            // attacks the target plant
            board.getPlants()[lane][col].takeDamage(damage);
        }
        else{
            // step the enemy towards the defences
            step();
        }
    }
    
    @Override
    public void draw(Graphics g){
        // Y-Axis Positioning
        int yPos = lane * GameBoard.CELL_HEIGHT + 15;
        
        if(sprite != null){
            g.drawImage(sprite, (int) x, yPos, 60, 70, null);
        }
        // Fallback Vector Rendering (Executes dynamically if the image asset fails to load)
        else{
            // Draws a placeholder rectangle (represents a popup)
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect((int) x, yPos + 5, 45, 50);
            g.setColor(Color.RED);
            g.drawRect((int) x, yPos + 5, 45, 50);
            
            // placeholder text inside the vector box bounds
            g.setFont(new Font("Monospaced", Font.BOLD, 12));
            g.drawString("AD!", (int) x + 12, yPos + 35);
        }
    }
}