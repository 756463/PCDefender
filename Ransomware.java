import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Ransomware extends Enemy{
    private int drainCooldown = 0;
    private static Image sprite;
    
    public Ransomware(double startX, int lane){
        // Sets enemy stats: 120 HP, 1 speed, 1 damage
        super(120, 1.0, 1, startX, lane);
        
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/Ransomware.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load Ransomware.png");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        int col = (int)(x / GameBoard.CELL_WIDTH);
        
        // Advances forward until tower, where it starts to attack
        if(col >= 0 && col < GameBoard.COLS && board.getPlants()[lane][col] != null){
            Tower target = board.getPlants()[lane][col];
            target.takeDamage(damage);
            
            // Steals the player's energy during attack
            drainCooldown++;
            if(drainCooldown >= 30){ 
                board.addSunPoints(-15); 
                drainCooldown = 0;
            }
        }
        else{
            step();
        }
    }
    
    @Override
    public void draw(Graphics g){
        int yPos = lane * GameBoard.CELL_HEIGHT + 15;
        
        // Draws the sprite image, but if that image does not exist, it draws placeholder art
        if(sprite != null){
            g.drawImage(sprite,(int) x, yPos, 60, 70, null);
        }
        else{
            g.setColor(new Color(75, 0, 130)); 
            g.fillRect((int) x, yPos + 5, 40, 55);
            g.setColor(Color.GREEN);
            g.drawString("$",(int) x + 15, yPos + 40);
        }
    }
}