import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class ZipBomb extends Enemy{
    // Shared static sprite
    private static Image sprite;
	
    public ZipBomb(double startX, int lane){
        // Sets enemy stats: 80 HP, 1.4 speed, 1 damage
        super(80, 1.4, 1, startX, lane);
        
        if(sprite == null){
            try{
                sprite = new ImageIcon(getClass().getResource("/ZipBomb.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load ZipBomb.png");
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
	
    public void triggerDetonationPayload(GameBoard board){
        int myCol =(int)(x / GameBoard.CELL_WIDTH);
        
        //Deals 150 damage after dying
        for(int c = myCol - 1; c <= myCol + 1; c++){
            if(c >= 0 && c < GameBoard.COLS){
                if(board.getPlants()[lane][c] != null){
                    board.getPlants()[lane][c].takeDamage(150); 
                }
            }
        }
    }
	
    @Override
    public void draw(Graphics g){
        int yPos = lane * GameBoard.CELL_HEIGHT + 15;
        
        //Draws the sprite image, but if that image does not exist, it draws placeholder art
        if(sprite != null){
            g.drawImage(sprite,(int) x, yPos, 60, 70, null);
        }
        else{
            g.setColor(Color.ORANGE.darker());
            g.fillRect((int) x, yPos + 10, 45, 45);
            g.setColor(Color.BLACK);
            g.drawString(".ZIP",(int) x + 10, yPos + 35);
        }
    }
}