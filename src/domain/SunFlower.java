package domain;

public class SunFlower extends Plant {
    private static final int SUN_PRODUCTION = 25;  // Cantidad de soles generados
    private static final int PRODUCTION_TIME_MS = 20 * 1000;  // Tiempo de producción en milisegundos (20 segundos)
    private static final int MIN_HEALTH = 0;  // Salud mínima (muerta)

    private long lastSunTime; // Última vez que generó soles

    public SunFlower(int row, int col) {
        this.health = 300;
        this.sunCost = 50;
        this.positionX = row;
        this.positionY = col;
        this.lastSunTime = System.currentTimeMillis();  // Inicializar la última vez que generó soles
    }


    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }
}
