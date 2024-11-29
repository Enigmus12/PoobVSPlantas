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

    /**
     * Generar soles
     * @return cantidad de soles generados
     */
    public int generateSuns() {
        long currentTime = System.currentTimeMillis();

        // Solo generar soles si la planta está viva y han pasado los 20 segundos
        if (health > MIN_HEALTH && (currentTime - lastSunTime >= PRODUCTION_TIME_MS)) {
            lastSunTime = currentTime;  // Actualizar el tiempo de generación
            return SUN_PRODUCTION;  // Generar 25 soles
        }
        return 0;  // No generar soles si no pasaron 20 segundos o si la planta está muerta
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }
}
