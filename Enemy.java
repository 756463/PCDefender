import java.awt.Graphics;

public abstract class Enemy {
    // Protected attributes accessible by sub-classes
    protected int health;
    protected double speed;
    protected int damage;
    protected double x;    // Horizontal position tracking on the game screen
    protected int lane;    // The vertical row layout index (0 to 4) where this enemy travels
    
    public Enemy(int health, double speed, int damage, double x, int lane){
        // Base constructor assigning attributes upon enemy spawning
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.x = x;
        this.lane = lane;
    }
    
    // Abstract rules: forcing every unique enemy type to write its own update and draw methods
    public abstract void update(GameBoard board);
    public abstract void draw(Graphics g);
    
    // getter-setter methods
    public int getHealth(){return health;}
    public void takeDamage(int amount){this.health -= amount;}
    public double getX(){return x;}
    public int getLane(){return lane;}
    
    // Horizontal step: enemies move towards defences (left)
    public void step(){this.x -= speed;}
}