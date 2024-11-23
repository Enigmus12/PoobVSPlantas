package domain;

public class Peashooter extends Plant implements IAttacker {
    private int shootSpeed;
    private int projectileDamage;
    
    public Peashooter() {
        this.health = 100;
        this.sunCost = 100;
        this.shootSpeed = 1;
        this.projectileDamage = 20;
        this.rechargeTime = 7;
    }
    
    @Override
    public void attack() {
        // Implementación del ataque
    }
    
    /**
     * Genera un proyectil para el ataque
     */
    public void generateProjectile() {
        // Lógica para crear y lanzar proyectil
    }

}
