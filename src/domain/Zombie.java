package domain;

public abstract class Zombie extends Character {
    protected int speed; // Velocidad de movimiento del zombie
    protected int damage = 100; // Daño base del zombie
    protected int health;
    public Zombie(int row, int col) {
        this.positionX = row;
        this.positionY = col;
    }
    public void move(){
        this.positionX -= 1;
    }

    public boolean isAlive(){
        return health > 0;
    }

    public void strikePea() {
        this.health -= 20;
    }

    public int getDamage() {
        return damage;
    }

    public void attack(Character target) {
        if (target instanceof Plant) {
            target.receiveDamage(damage);
        }
    }


}