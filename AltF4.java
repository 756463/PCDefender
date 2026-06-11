import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class AltF4 extends Tower{
    //alt f4 tower is supposed to act like the cherry bomb, 
    //fuse time and image variable
    private int fuseTimer = 20; 
    private static Image sprite;
    
    public AltF4(int lane, int col){
        //health is set to 99999 so enemies cannot destroy the tower
        super(99999, 150, lane, col);
        
        //add sprite to the image loaded for the tower
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/AltF4.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load AltF4.png");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        //tick the fuse down every time board is updated
        fuseTimer--;
        if(fuseTimer <= 0){
            //Find the center point x-coordinate of the occupied tile
            int targetX = this.col * GameBoard.CELL_WIDTH +(GameBoard.CELL_WIDTH / 2);
            
            //Iterate through every single enemy alive on the board
            for(Enemy e : board.getZombies()){
                
                 //Math.abs(e.getLane() - this.lane) <= 1 checks if the enemy is in:
                 //The lane directly above (-1)
                 //The exact same lane (0)
                 //The lane directly below (+1)
                
                if(Math.abs(e.getLane() - this.lane) <= 1){
                    //Horizontal proximity check: calculates distance between enemy and center of explosion
                    double distance = Math.abs(e.getX() - targetX);
                    
                     //If enemy is within 1.5 cells wide horizontally, apply 500 damage points (nukes most malware).
                    if(distance <= GameBoard.CELL_WIDTH * 1.5){
                        e.takeDamage(500); 
                    }
                }
            }
             //destroy itself after exploding
            this.health = 0; 
        }
    }
    
    @Override
    public void draw(Graphics g, int x, int y){
         //Renders the sprite if found; otherwise drops down to vector shapes.
        if(sprite != null){
            g.drawImage(sprite, x + 5, y + 5, GameBoard.CELL_WIDTH - 10, GameBoard.CELL_HEIGHT - 10, null);
        }
        else{
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x + 10, y + 10, 70, 70);
            g.setColor(Color.RED);
            g.drawString("ALT + F4", x + 20, y + 45);
        }
    }
}