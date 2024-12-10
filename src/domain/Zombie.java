package domain;

public abstract class Zombie extends Character {
    protected int speed; // Velocidad de movimiento del zombie
    protected int damage; // Daño base del zombie
    
    public Zombie(int row, int col) {
        this.positionX = row;
        this.positionY = col;
        // Inicializar salud y velocidad según el tipo de zombie
        this.damage = 10; // Daño base predeterminado
    }

    /**
     * Método de ataque estándar para zombies
     * @param target La planta objetivo del ataque
     */
    public void attack(Plant target) {
        if (target != null) {
            target.receiveDamage(this.damage);
        }
    }

}