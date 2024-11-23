package domain;

public abstract class Zombie extends Character {
    protected float speed;
    
    /**
     * Nos sirve para que el zombie avance en el tablero
     */
    public abstract void advance();
    
    /**
     * Realiza el ataque del zombie
     */
    public abstract void attack();
    
    /**
     * Verifica colisiones con otros elementos
     */
    public abstract void checkCollision();
}

