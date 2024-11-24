package domain;

public abstract class Character {
    protected int positionX;
    protected int positionY;
    protected int health;
    protected String imagePath;
    
    /**
     * Verificamos si el personaje está vivo
     * @return true si el personaje tiene vida mayor a 0
     */
    public boolean isAlive() {
        return health > 0;
    }
    
    /**
     * Método para recibir daño
     * @param amount cantidad de daño a recibir
     */
    public void receiveDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) this.health = 0;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public int getPositionX() { 
        return positionX; 
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public int getPositionY() { 
        return positionY; 
    }
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    public int getHealth() { return health; }
}
