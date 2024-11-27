package domain;

public class Peashooter extends Plant implements IAttacker {
    private double shootSpeed;
    private int projectileDamage;
    
    public Peashooter(int row, int col) {
        this.health = 100;
        this.sunCost = 100;
        this.shootSpeed = 1.5;
        this.projectileDamage = 20;
        this.positionX = row;
        this.positionY = col; 
    }
    
    @Override
    public void attack() {
    }
    
    /**
     * Genera un proyectil para el ataque
     */
    public void generateProjectile() {
    }

}
