import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Mouse extends Tower{
    // trackers for firerate and animation
    private int clickCooldown = 0;
    private int firingAnimationTimer = 0;
    
    // Shared static sprites for firing and not firing states
    private static Image normalSprite;
    private static Image clickSprite;
    
    public Mouse(int lane, int col){
        super(100, 100, lane, col); // Sets health and grid alignment
        
        // attempt to load image from file
        if(normalSprite == null){
            try{
                normalSprite = new ImageIcon(getClass().getResource("/Mouse.png")).getImage();
                clickSprite = new ImageIcon(getClass().getResource("/MouseClick.png")).getImage();
            }
            catch(Exception e){
                System.out.println("Could not load Mouse image assets.");
            }
        }
    }
    
    @Override
    public void update(GameBoard board){
        // tick timers down
        if(clickCooldown > 0){
            clickCooldown--;
        }
        if(firingAnimationTimer > 0){
            firingAnimationTimer--;
        }
        
        // Attack Validation: Scans for active enemies when attack cycle finishes resetting
        if(clickCooldown <= 0){
            boolean targetDetected = false;
            for(Enemy e : board.getZombies()){
                // Conditions: Enemy must share the same lane and be on the right side
                if(e.getLane() == this.lane && e.getX() > this.col * GameBoard.CELL_WIDTH){
                    targetDetected = true;
                    break;
                }
            }
            
            // Firing Action Sequence: Generates a projectile
            if(targetDetected){
                double spawnX = this.col * GameBoard.CELL_WIDTH + 60; // Offset fire coordinates forward
                board.addProjectile(new ClickProjectile(spawnX, this.lane, 20));
                
                clickCooldown = 45;       // Reset attack delay
                firingAnimationTimer = 10; // Reset firing animation
            }
        }
    }

    @Override
    public void draw(Graphics g, int x, int y){
        // Toggle active sprite based on firing animation state 
        Image activeSprite = (firingAnimationTimer > 0) ? clickSprite : normalSprite;
        
        if(activeSprite != null){
            g.drawImage(activeSprite, x + 5, y + 5, GameBoard.CELL_WIDTH - 10, GameBoard.CELL_HEIGHT - 10, null);
        }
        // Fallback vector rendering (Executes if source files are missing or broken)
        else{
            g.setColor(firingAnimationTimer > 0 ? Color.RED : Color.WHITE);
            g.fillOval(x + 25, y + 20, 40, 60); 
            g.setColor(Color.GRAY);
            g.drawLine(x + 45, y + 20, x + 45, y + 50); 
        }
    }
}