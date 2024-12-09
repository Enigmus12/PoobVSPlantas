package domain;

public class BasicZombie extends Zombie {
    public BasicZombie(int row, int col) {
        super(row, col);
        this.health = 100; // Salud inicial
        this.speed = 1; 
        this.damage = 10; // Daño específico para zombie básico
    }

    @Override
    public void advance() {

    }

    @Override
    public void attack() {

    }

    @Override
    public void checkCollision() {

    }
}