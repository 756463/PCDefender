import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Antivirus extends Tower{
    //Shared static image sprite
    private static Image sprite;
    
    public Antivirus(int lane, int col){
        //Sets tower stats: 150 Health, 175 Energy Cost
        super(150, 175, lane, col);
        
        //Load image if it hasn't been loaded yet
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/Antivirus.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load Antivirus.png");
            }
        }
    }

    @Override
    public void update(GameBoard board){
        //Calculate the horizontal boundary lines of this specific grid cell
        int myLeftX = this.col * GameBoard.CELL_WIDTH;
        int myRightX = myLeftX + GameBoard.CELL_WIDTH;
        
        //Loop through all active projectiles on screen
        for(ClickProjectile p : board.getProjectiles()){
            //Check if the projectile is passing through this tower's cell and lane
            if(p.getLane() == this.lane && p.getX() >= myLeftX && p.getX() <= myRightX){
                //If the projectile isn't powered up yet, boost its damage
                if(!p.isAmplified()){
                    p.amplify();
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics g, int x, int y){
        //Draw the sprite if available; otherwise, render a vector fallback shape
        if(sprite != null){
            g.drawImage(sprite, x + 5, y + 5, GameBoard.CELL_WIDTH - 10, GameBoard.CELL_HEIGHT - 10, null);
        }
        else{
            g.setColor(Color.BLUE.brighter());
            g.fillRoundRect(x + 20, y + 20, 50, 60, 15, 15);
            g.setColor(Color.CYAN);
            g.drawRoundRect(x + 15, y + 15, 60, 70, 15, 15);
        }
    }
}