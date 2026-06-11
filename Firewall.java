import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Firewall extends Tower{
    // Shared static sprite
    private static Image sprite;
    
    public Firewall(int lane, int col){
        // Sets wall stats: 400 high Health, 50 low Energy Cost
        super(400, 50, lane, col);
        //load custom sprite
        if(sprite == null){
            URL imgURL = getClass().getResource("/Firewall.png");
            
            if(imgURL != null){
                sprite = new ImageIcon(imgURL).getImage();
            }
            else{
                System.out.println("ALERT: /Firewall.png could not be found! Using vector fallback.");
                sprite = null; 
            }
        }
    }

    @Override
    public void update(GameBoard board){
        //blank because tower does not move/attack
    }
    
    @Override
    public void draw(Graphics g, int x, int y){
        if(sprite != null){
            // Renders the image asset scaled down within tile border
            g.drawImage(sprite, x + 5, y + 5, GameBoard.CELL_WIDTH - 10, GameBoard.CELL_HEIGHT - 10, null);
        }
        else{
            //vector render fallback if no image found
            g.setColor(new Color(139, 0, 0)); 
            g.fillRect(x + 15, y + 10, GameBoard.CELL_WIDTH - 30, GameBoard.CELL_HEIGHT - 20);
            
            g.setColor(Color.RED);
            g.drawRect(x + 15, y + 10, GameBoard.CELL_WIDTH - 30, GameBoard.CELL_HEIGHT - 20);
            
            g.setColor(Color.WHITE);
            g.drawString("FW: " + health, x + 25, y + 55);
        }
    }
}