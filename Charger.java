import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Charger extends Tower{
    //Tracks time between resource drops (energy)
    private int generationTimer = 0;
    //Shared static sprite
    private static Image sprite;
    
    public Charger(int lane, int col){
        //Sets tower stats: 100 Health, 50 Energy Cost
        super(100, 50, lane, col);
        
        //Load image if it hasn't been loaded yet
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/Charger.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load Charger.png");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        //Tick the resource timer up
        generationTimer++;
        //At 150 frames, generate energy and reset the timer
        if(generationTimer >= 150){ 
            board.addSunPoints(25); 
            generationTimer = 0;
        }
    }
    
    @Override
    public void draw(Graphics g, int x, int y){
        //Draw the sprite if available; otherwise, render a yellow vector shape
        if(sprite != null){
            g.drawImage(sprite, x + 5, y + 5, GameBoard.CELL_WIDTH - 10, GameBoard.CELL_HEIGHT - 10, null);
        }
        else{
            g.setColor(Color.YELLOW);
            g.fillOval(x + 25, y + 25, 40, 40);
            g.setColor(Color.BLACK);
            g.fillOval(x + 35, y + 35, 20, 20); 
        }
    }
}