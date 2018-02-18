package jetpack;

public class Obstacle {
    
    private int x;
    private int y;
    private int size;
    private MovePattern movepattern;
    private int oldX;
    
    public Obstacle(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.oldX = x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public MovePattern getMovePattern() {
        return movepattern;
    }
    
    public int getOldX() {
        return this.oldX;
    }
    
    public void setOldX(int oldx) {
        this.oldX = oldx;
    }
    
}
