package domain;

public class BasicZombie extends Zombie {
    public BasicZombie(int row, int col) {
        super(row, col);
        this.health = 100; // Salud inicial
        this.damage = 10; // Daño específico para zombie básico
    }

}