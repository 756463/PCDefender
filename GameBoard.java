import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel implements ActionListener{
    // grid dimensions
    public static final int LANES = 5;
    public static final int COLS = 9;
    public static final int CELL_WIDTH = 90;
    public static final int CELL_HEIGHT = 100;
    
    // Tracks which button the player has selected from the bottom shop menu
    private enum SelectedUnit{ NONE, MOUSE, CHARGER, ALT_F4, WALNUT, ANTIVIRUS, RECYCLING_BIN }
    private SelectedUnit currentSelection = SelectedUnit.NONE;
    
    // shop coordinates
    private final int SHOP_Y = LANES * CELL_HEIGHT + 5;
    private final int BUTTON_WIDTH = 80;
    private final int BUTTON_HEIGHT = 50;
    
    //core lists
    private Timer gameTimer;
    private List<Enemy> enemies;
    private Tower[][] defenses; 
    private List<ClickProjectile> projectiles = new ArrayList<>();
    
    // player data and energy
    private int energyPoints;
    private int currentLevel = 1;
    private final int totalLevels = 5;
    
    // wave control for enemies
    private int currentWave = 0;
    private int totalWavesInLevel;
    private int enemiesPerWave;
    private int enemiesSpawnedInCurrentWave = 0;
    
    // wave cooldown
    private int spawnDelayTimer = 0;
    private boolean waveActive = false;
    private int betweenWaveTimer = 0;
    
    private PCDefenderApp app;
    
    public GameBoard(PCDefenderApp app, int selectedLevel){
        this.app = app;
        this.currentLevel = selectedLevel;
        
        // window size based on lanes
        setPreferredSize(new Dimension(COLS * CELL_WIDTH, LANES * CELL_HEIGHT + 60));
        setBackground(Color.GREEN.darker());
        
        enemies = new ArrayList<>();
        defenses = new Tower[LANES][COLS];
        
        //game's internal clock
        gameTimer = new Timer(33, this);
        gameTimer.start();
        
        initLevel(currentLevel);
        
        // Automatically translates click coordinates into either a Shop click or a Grid cell location.
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                int mouseX = e.getX();
                int mouseY = e.getY();
                
                // user clicks within the shop menu/bottom menu
                if(mouseY >= SHOP_Y && mouseY <= SHOP_Y + BUTTON_HEIGHT){
                    if(mouseX >= 15 && mouseX <= 15 + BUTTON_WIDTH){
                        currentSelection = SelectedUnit.MOUSE;
                    }
                    else if(mouseX >= 110 && mouseX <= 110 + BUTTON_WIDTH){
                        currentSelection = SelectedUnit.CHARGER;
                    } 
                    else if(mouseX >= 205 && mouseX <= 205 + BUTTON_WIDTH && currentLevel >= 3){
                        currentSelection = SelectedUnit.ALT_F4;
                    } 
                    else if(mouseX >= 300 && mouseX <= 300 + BUTTON_WIDTH && currentLevel >= 2){
                        currentSelection = SelectedUnit.WALNUT; // WALNUT maps to the Firewall object instantiation
                    } 
                    else if(mouseX >= 395 && mouseX <= 395 + BUTTON_WIDTH && currentLevel >= 3){
                        currentSelection = SelectedUnit.ANTIVIRUS;
                    } 
                    else if(mouseX >= 490 && mouseX <= 490 + BUTTON_WIDTH){
                        currentSelection = SelectedUnit.RECYCLING_BIN;
                    }
                    repaint();
                    return;
                }
                
                // user clicks within the game field
                int lane = mouseY / CELL_HEIGHT; // Maps Y pixel coordinate to a vertical grid row (0-4)
                int col = mouseX / CELL_WIDTH;   // Maps X pixel coordinate to a horizontal grid column (0-8)
                
                if(lane >= 0 && lane < LANES && col >= 0 && col < COLS){
                    // recycle bin
                    if(currentSelection == SelectedUnit.RECYCLING_BIN){
                        if(defenses[lane][col] != null){
                            defenses[lane][col] = null;
                        }
                        currentSelection = SelectedUnit.NONE;
                    } 
                    // 
                    else if(defenses[lane][col] == null){
                        if(currentSelection == SelectedUnit.MOUSE && energyPoints >= 100){
                            defenses[lane][col] = new Mouse(lane, col);
                            energyPoints -= 100;
                            currentSelection = SelectedUnit.NONE;
                        } 
                        else if(currentSelection == SelectedUnit.CHARGER && energyPoints >= 50){
                            defenses[lane][col] = new Charger(lane, col);
                            energyPoints -= 50;
                            currentSelection = SelectedUnit.NONE;
                        }
                        else if(currentSelection == SelectedUnit.ALT_F4 && energyPoints >= 150){
                            defenses[lane][col] = new AltF4(lane, col); 
                            energyPoints -= 150;
                            currentSelection = SelectedUnit.NONE;
                        }
                        else if(currentSelection == SelectedUnit.WALNUT && energyPoints >= 50){
                            defenses[lane][col] = new Firewall(lane, col); 
                            energyPoints -= 50;
                            currentSelection = SelectedUnit.NONE;
                        }
                        else if(currentSelection == SelectedUnit.ANTIVIRUS && energyPoints >= 150){
                            defenses[lane][col] = new Antivirus(lane, col); 
                            energyPoints -= 150;
                            currentSelection = SelectedUnit.NONE;
                        }
                    }
                }
                repaint();
            }
        });
        
        initLevel(currentLevel);
    }
    
     //Dynamically sets waves, spawn caps, and starter energy per level
    private void initLevel(int level){
        enemies.clear();
        projectiles.clear();
        defenses = new Tower[LANES][COLS];
        currentSelection = SelectedUnit.NONE;
        
        currentWave = 0;
        enemiesSpawnedInCurrentWave = 0;
        waveActive = false;
        betweenWaveTimer = 200; 
        
        switch(level){
            case 1:
                totalWavesInLevel = 3;
                enemiesPerWave = 4;
                energyPoints = 150; 
                break;
            case 2:
                totalWavesInLevel = 3;
                enemiesPerWave = 5;
                energyPoints = 200;
                break;
            case 3:
                totalWavesInLevel = 4;
                enemiesPerWave = 6;
                energyPoints = 250;
                break;
            case 4:
                totalWavesInLevel = 4;
                enemiesPerWave = 7;
                energyPoints = 300;
                break;
            case 5:
                totalWavesInLevel = 5;
                enemiesPerWave = 8;
                energyPoints = 350;
                break;
            default:
                totalWavesInLevel = 3;
                enemiesPerWave = 5;
                energyPoints = 200;
        }
    }
    
    private void startNextWave(){
        currentWave++;
        enemiesSpawnedInCurrentWave = 0;
        spawnDelayTimer = 0;
        waveActive = true;
    }
    

    @Override
    public void actionPerformed(ActionEvent e){
        // Waves timing countdown trigger
        if(!waveActive && enemies.isEmpty()){
            betweenWaveTimer--;
            if(betweenWaveTimer <= 0){
                if(currentWave < totalWavesInLevel){
                    startNextWave();
                }
            }
        }
        
        // Spawn enemy logic matching the current active level configuration rules
        if(waveActive){
            spawnDelayTimer++;
            int spawnInterval = Math.max(45, 100 -(currentLevel * 10)); 
            
            if(spawnDelayTimer >= spawnInterval && enemiesSpawnedInCurrentWave < enemiesPerWave){
                int randomLane =(int)(Math.random() * LANES);
                double rng = Math.random();
                
                if(currentLevel == 1){
                    enemies.add(new Popup(getWidth(), randomLane));
                }
                else if(currentLevel == 2){
                    if(rng > 0.5) enemies.add(new USBKiller(getWidth(), randomLane));
                    else enemies.add(new Popup(getWidth(), randomLane));
                }
                else if(currentLevel == 3){
                    if(rng > 0.6) enemies.add(new Ransomware(getWidth(), randomLane));
                    else if(rng > 0.3) enemies.add(new USBKiller(getWidth(), randomLane));
                    else enemies.add(new Popup(getWidth(), randomLane));
                }
                else if(currentLevel == 4){
                    if(rng > 0.7) enemies.add(new ZipBomb(getWidth(), randomLane));
                    else if(rng > 0.4) enemies.add(new Ransomware(getWidth(), randomLane));
                    else if(rng > 0.2) enemies.add(new USBKiller(getWidth(), randomLane));
                    else enemies.add(new Popup(getWidth(), randomLane));
                }
                else{
                    if(rng > 0.8) enemies.add(new Hacker(getWidth(), randomLane));
                    else if(rng > 0.6) enemies.add(new ZipBomb(getWidth(), randomLane));
                    else if(rng > 0.4) enemies.add(new Ransomware(getWidth(), randomLane));
                    else if(rng > 0.2) enemies.add(new USBKiller(getWidth(), randomLane));
                    else enemies.add(new Popup(getWidth(), randomLane));
                }
                
                enemiesSpawnedInCurrentWave++;
                spawnDelayTimer = 0;
            }
            
            if(enemiesSpawnedInCurrentWave >= enemiesPerWave){
                waveActive = false;
                betweenWaveTimer = 300; 
            }
        }
        
        // Checks defences and deletes dead towers
        for(int l = 0; l < LANES; l++){
            for(int c = 0; c < COLS; c++){
                if(defenses[l][c] != null){
                    defenses[l][c].update(this);
                    if(defenses[l][c] != null && defenses[l][c].getHealth() <= 0) defenses[l][c] = null; 
                }
            }
        }
        
        // Checks projectiles and hitboxes
        List<ClickProjectile> destroyedProjectiles = new ArrayList<>();
        for(ClickProjectile p : projectiles){
            p.update(this);
            if(p.getX() > getWidth()) destroyedProjectiles.add(p);
        }
        projectiles.removeAll(destroyedProjectiles);
        
        // Checks enemies and game overs
        List<Enemy> deadEnemies = new ArrayList<>();
        for(Enemy en : enemies){
            en.update(this);
            if(en.getHealth() <= 0){
                deadEnemies.add(en);
                // Custom trigger check for special post-mortem payloads
                if(en instanceof ZipBomb){
                   ((ZipBomb) en).triggerDetonationPayload(this);
                }
            }
            // Ends level if zombie got past defences
            if(en.getX() <= 0){
                gameTimer.stop();
                JOptionPane.showMessageDialog(this, "The Malware breached your defenses!\nGame Over.");
                app.showLevelSelect(); 
                return;
            }
        }
        enemies.removeAll(deadEnemies);
        
        // Checks if level is complete
        if(currentWave >= totalWavesInLevel && enemies.isEmpty() && !waveActive){
            gameTimer.stop();
            if(currentLevel < totalLevels){
                JOptionPane.showMessageDialog(this, "Node " + currentLevel + " Cleaned!");
                app.updatePlayerProgress(currentLevel);
                app.showLevelSelect();
            }
            else{
                JOptionPane.showMessageDialog(this, "Congratulations! System clean across all nodes!");
                app.updatePlayerProgress(currentLevel);
                app.showLevelSelect();
            }
            return;
        }
        repaint();
    }
    

     // Handles grid line calculations and displays menu elements
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        // Background tile rendering
        g.setColor(new Color(0, 40, 0)); 
        for(int l = 0; l <= LANES; l++) g.drawLine(0, l * CELL_HEIGHT, COLS * CELL_WIDTH, l * CELL_HEIGHT);
        for(int c = 0; c <= COLS; c++) g.drawLine(c * CELL_WIDTH, 0, c * CELL_WIDTH, LANES * CELL_HEIGHT);
        
        // Layer 1: Draw towers onto the grid
        for(int l = 0; l < LANES; l++){
            for(int c = 0; c < COLS; c++){
                if(defenses[l][c] != null) defenses[l][c].draw(g, c * CELL_WIDTH, l * CELL_HEIGHT);
            }
        }
        
        // Layer 2: Draw traveling weapon projectiles
        for(ClickProjectile p : projectiles){
            p.draw(g); 
        }
        
        // Layer 3: Draw enemies over grid components
        for(Enemy en : enemies){
            en.draw(g);
        }
        
        // Layer 4: Shop Panel HUD Graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, LANES * CELL_HEIGHT, getWidth(), 60);
        
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        
        // Button 1: Mouse (fires projectiles)
        int x1 = 15;
        if(currentSelection == SelectedUnit.MOUSE){
            g.setColor(Color.WHITE); g.fillRect(x1 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
        }
        g.setColor(energyPoints >= 100 ? Color.DARK_GRAY : new Color(100, 0, 0));
        g.fillRect(x1, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.GREEN);
        g.drawString("Mouse", x1 + 22, SHOP_Y + 22);
        g.drawString("100 E", x1 + 22, SHOP_Y + 38);

        // Button 2: Charger (generates energy)
        int x2 = 110;
        if(currentSelection == SelectedUnit.CHARGER){
            g.setColor(Color.WHITE); g.fillRect(x2 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
        }
        g.setColor(energyPoints >= 50 ? Color.DARK_GRAY : new Color(100, 0, 0));
        g.fillRect(x2, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.YELLOW);
        g.drawString("Charger", x2 + 16, SHOP_Y + 22);
        g.drawString("50 E", x2 + 25, SHOP_Y + 38);

        // Button 3: Alt F4 Bomb (Unlocks at level 3)
        int x3 = 205;
        if(currentLevel >= 3){
            if(currentSelection == SelectedUnit.ALT_F4){
                g.setColor(Color.WHITE); g.fillRect(x3 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
            }
            g.setColor(energyPoints >= 150 ? Color.DARK_GRAY : new Color(100, 0, 0));
            g.fillRect(x3, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(Color.RED);
            g.drawString("Alt F4", x3 + 19, SHOP_Y + 22);
            g.drawString("150 E", x3 + 22, SHOP_Y + 38);
        }
        else{
            g.setColor(new Color(40, 40, 40));
            g.fillRect(x3, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(Color.GRAY);
            g.drawString("LOCKED", x3 + 19, SHOP_Y + 30);
        }
        
        // Button 4: Firewall (Unlocks at level 2)
        int x4 = 300;
        if(currentLevel >= 2){
            if(currentSelection == SelectedUnit.WALNUT){
                g.setColor(Color.WHITE); g.fillRect(x4 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
            }
            g.setColor(energyPoints >= 50 ? Color.DARK_GRAY : new Color(100, 0, 0));
            g.fillRect(x4, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(new Color(222, 184, 135)); 
            g.drawString("Firewall", x4 + 13, SHOP_Y + 22);
            g.drawString("50 E", x4 + 25, SHOP_Y + 38);
        }
        else{
            g.setColor(new Color(40, 40, 40));
            g.fillRect(x4, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(Color.GRAY);
            g.drawString("LOCKED", x4 + 13, SHOP_Y + 30);
        }
        
        // Button 5: Antivirus (Unlocks at level 3)
        int x5 = 395;
        if(currentLevel >= 3){
            if(currentSelection == SelectedUnit.ANTIVIRUS){
                g.setColor(Color.WHITE); g.fillRect(x5 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
            }
            g.setColor(energyPoints >= 150 ? Color.DARK_GRAY : new Color(100, 0, 0));
            g.fillRect(x5, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(Color.CYAN);
            g.drawString("AntiVir", x5 + 16, SHOP_Y + 22);
            g.drawString("150 E", x5 + 22, SHOP_Y + 38);
        }
        else{
            g.setColor(new Color(40, 40, 40));
            g.fillRect(x5, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            g.setColor(Color.GRAY);
            g.drawString("LOCKED", x5 + 16, SHOP_Y + 30);
        }
        
        // Button 6: Recycling Bin
        int x6 = 490;
        if(currentSelection == SelectedUnit.RECYCLING_BIN){
            g.setColor(Color.WHITE); g.fillRect(x6 - 2, SHOP_Y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
        }
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x6, SHOP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Recycle", x6 + 16, SHOP_Y + 22);
        g.drawString("Bin", x6 + 28, SHOP_Y + 38);

        // Displays Energy balances
        g.setColor(Color.CYAN);
        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.drawString("ENERGY: " + energyPoints, 585, LANES * CELL_HEIGHT + 25);
        g.drawString("NODE: " + currentLevel + "/" + totalLevels, 585, LANES * CELL_HEIGHT + 45);
        
        // monitor the current wave state
        g.setColor(Color.LIGHT_GRAY);
        if(currentWave == totalWavesInLevel && enemies.isEmpty()){
            g.drawString("LEVEL SECURED!", 710, LANES * CELL_HEIGHT + 35);
        }
        else if(!waveActive && enemies.isEmpty()){
            g.drawString("NEXT SURGE: " +(betweenWaveTimer / 30) + "s", 710, LANES * CELL_HEIGHT + 35);
        }
        else{
            g.drawString("WAVE SURGE: " + currentWave + "/" + totalWavesInLevel, 710, LANES * CELL_HEIGHT + 35);
        }
    }
    
    // data helpers; getter/setters
    public List<ClickProjectile> getProjectiles(){ return projectiles; }
    public void addProjectile(ClickProjectile p){ this.projectiles.add(p); }
    public void addSunPoints(int amount){ this.energyPoints += amount; }
    public List<Enemy> getZombies(){ return enemies; }
    public Tower[][] getPlants(){ return defenses; }
}