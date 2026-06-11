import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class ClickProjectile {
    //positioning, lane tracking, and movement
    private double x;
    private int lane;
    private int damage;
    private double speed = 7.0; //Horizontal pixels travelled per frame update
    private boolean amplified = false; //boosted damage state
    private static Image sprite;
    
    public ClickProjectile(double x, int lane, int damage) {
        //Assigns base horizontal position, target lane, and damage weight
        this.x = x;
        this.lane = lane;
        this.damage = damage;
        
        //Caches the image asset globally if not already stored
        if(sprite == null) {
            try{
                sprite = new ImageIcon(getClass().getResource("/Projectile.png")).getImage();
            }
            catch(Exception e) {
                System.out.println("Could not load Projectile.png");
            }
        }
    }
    
    public void update(GameBoard board) {
        //Move the projectile forward horizontally across the grid
        x += speed;
        
        //Scan for collisions with enemies on the game board
        for(Enemy e : board.getZombies()) {
            //Check if enemy matches lane and falls within a 20-pixel hit detection window
            if(e.getLane() == this.lane && Math.abs(e.getX() - this.x) < 20) {
                //Apply double damage if amplified by an Antivirus unit; else base damage
                e.takeDamage(amplified ? damage * 2 : damage);
                //Teleport out-of-bounds to trigger removal
                this.x = 99999; 
                break;
            }
        }
    }
    
    public void draw(Graphics g) {
        //Center the coordinate vertically within the targeted lane boundary
        int yPos = lane * GameBoard.CELL_HEIGHT +(GameBoard.CELL_HEIGHT / 2) - 8;
        //Render image if present; otherwise default to a color-coded fallback circle
        if(sprite != null) {
            g.drawImage(sprite,(int) x, yPos, 16, 16, null);
        }
        else{
            g.setColor(amplified ? Color.ORANGE : Color.CYAN);
            g.fillOval((int) x, yPos, 12, 12);
        }
    }
    
    //data access and modification helpers
    public double getX() { return x; }
    public int getLane() { return lane; }
    public void amplify() { this.amplified = true; }
    public boolean isAmplified() { return amplified; }
}