package domain;

public abstract class Zombie extends Character {
    protected int speed; // Velocidad de movimiento del zombie
    protected int damage; // Daño base del zombie
    protected int health;
    public Zombie(int row, int col) {
        this.positionX = row;
        this.positionY = col;
        this.damage = 10; // Daño base predeterminado
    }
    public void move(){
        this.positionX -= 1;
    }

    public boolean isAlive(){
        return health <= 0;
    }

    public void strikePea() {
        this.health -=20;
    }


}