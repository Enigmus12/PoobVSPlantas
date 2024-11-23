package domain;

public class BasicZombie extends Zombie {
    private int damage;
    
    public BasicZombie() {
        this.health = 100;
        this.speed = 1.0f;
        this.damage = 100;
    }
    
    @Override
    public void advance() {
        this.positionX -= speed;
    }
    
    @Override
    public void attack() {
    }
    
    @Override
    public void checkCollision() {
    }
}
