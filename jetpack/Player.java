
package jetpack;

public class Player {
    
    private final int x = 70;
    private int y = 672;
    private int oldY = 672;
    private final int size = 70;
    private int health;
    private boolean alive;
    private final int maxY = 672;
    private boolean upDirection = false;
    
    public int getX() {
        return this.x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public boolean getAlive() {
        return this.alive;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getOldY() {
        return this.oldY;
    }
    
    public void setOldY(int oldY) {
        this.oldY = oldY;
    }
    
    public int getMaxY() {
        return this.maxY;
    }
    
    public boolean getUpDirection() {
        return this.upDirection;
    }
    
    public void setUpDirection(boolean direction) {
        this.upDirection = direction;
    } 
    
}