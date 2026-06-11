import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class USBKiller extends Enemy{
    private static Image cleanSprite;
    private static Image brokenSprite;
    
    public USBKiller(double startX, int lane){
        // Sets enemy stats: 280 HP, 0.9 speed, 1 damage
        super(280, 0.9, 1, startX, lane);
        
        if(cleanSprite == null){
            try{
                cleanSprite = new ImageIcon(getClass().getResource("/USBKiller.png")).getImage();
                brokenSprite = new ImageIcon(getClass().getResource("/BrokenUSBKiller.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load USBKiller images.");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        int col =(int)(x / GameBoard.CELL_WIDTH);
        
        // Advances forward until tower, where it starts to attack
        if(col >= 0 && col < GameBoard.COLS && board.getPlants()[lane][col] != null){
            board.getPlants()[lane][col].takeDamage(damage);
        }
        else{
            step();
        }
    }
    
    @Override
    public void draw(Graphics g){
        int yPos = lane * GameBoard.CELL_HEIGHT + 15;
        
        // Changes sprite when armor is destroyed
        Image currentSprite = (this.health <= 100) ? brokenSprite : cleanSprite;
        
        // Draws the sprite image, but if that image does not exist, it draws placeholder art
        if(currentSprite != null){
            g.drawImage(currentSprite,(int) x, yPos, 65, 70, null);
        }
        else{
            g.setColor(health > 100 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            g.fillRect((int) x, yPos + 15, 50, 35);
            g.setColor(Color.YELLOW);
            g.fillRect((int) x + 50, yPos + 25, 15, 15); 
        }
    }
}