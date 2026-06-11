import java.awt.Graphics;

public abstract class Tower{
    // Damage it can take
    protected int health;
    
    // Energy required to place
    protected int cost;
    
    // Position on grid
    protected int lane;
    protected int col;
    
    public Tower(int health, int cost, int lane, int col){
        this.health = health;
        this.cost = cost;
        this.lane = lane;
        this.col = col;
    }
    
    // Main logic controller
    public abstract void update(GameBoard board);
    
    // Draws sprites and their variations
    public abstract void draw(Graphics g, int x, int y);
    
    // Called when enemies and their effects damage plants
    public void takeDamage(int amount){this.health -= amount;}
    
    // Getters for information
    public int getHealth(){return health;}
    public int getCost(){return cost;}
    public int getLane(){return lane;}
    public int getCol(){return col;}
}