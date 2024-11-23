package domain;

public class Peashooter extends Plant implements IAttacker {
    private double shootSpeed;
    private int projectileDamage;
    
    public Peashooter() {
        this.health = 100;
        this.sunCost = 300;
        this.shootSpeed = 1.5;
        this.projectileDamage = 20;
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
