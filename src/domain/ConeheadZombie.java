package domain;

public class ConeheadZombie extends Zombie {
    private int coneResistance;
    
    public ConeheadZombie() {
        this.health = 100;
        this.speed = 1.0f;
        this.coneResistance = 280;
    }
    
    
    public void loseCone() {
        this.coneResistance = 0;
    }
    
    @Override
    public void advance() {
        this.positionX -= speed;
    }
    
    @Override
    public void attack() {
        // Implementación del ataque
    }
    
    @Override
    public void checkCollision() {
        // Verificación de colisiones
    }
}
