package domain;

public abstract class Character {
    protected int positionX;
    protected int positionY;
    protected int health;

    
    /**
     * Verificamos si el personaje está vivo
     * @return true si el personaje tiene vida mayor a 0
     */
    public boolean isAlive() {
        return this.health > 0;
    }
    
    /**
     * Método para recibir daño
     * @param amount cantidad de daño a recibir
     */
    public void receiveDamage(int amount) {
        this.health -= amount;
    }
    
    /**
     * Actualiza la posición del personaje
     * @param x nueva posición en X
     * @param y nueva posición en Y
     */
    public void updatePosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
/**
     * Establece la salud del objeto.
     *
     * @param health La nueva salud del objeto.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Obtiene la salud actual del objeto.
     *
     * @return La salud actual del objeto.
     */
    public int getHealth() {
        return health;
    }

    // Métodos relacionados con la posición X

    /**
     * Obtiene la posición X actual del objeto en el tablero.
     *
     * @return La posición X actual del objeto.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Establece una nueva posición X para el objeto en el tablero.
     *
     * @param positionX La nueva posición X del objeto.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    // Métodos relacionados con la posición Y

    /**
     * Obtiene la posición Y actual del objeto en el tablero.
     *
     * @return La posición Y actual del objeto.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Establece una nueva posición Y para el objeto en el tablero.
     *
     * @param positionY La nueva posición Y del objeto.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    

}
