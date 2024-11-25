package domain;

public class Sunflower extends Plant {
    private int sunProduction;
    private int productionTime;
    private long lastSunTime; // Última vez que generó soles
    
    public Sunflower(int row, int col) {
        this.health = 300;
        this.sunCost = 50;
        this.sunProduction = 25;
        this.productionTime = 20; // segundos
        this.rechargeTime = 7; // segundos
        this.positionX = row;
        this.positionY = col;
    }
    public int generateSuns() {
        long currentTime = System.currentTimeMillis();
        // Verificar si pasaron 20 segundos desde la última generación
        if (currentTime - lastSunTime >= 20 * 1000) {
            lastSunTime = currentTime; // Actualizar el tiempo de generación
            return 25; // Generar 25 soles
        }
        return 0; // No generar soles si no pasaron 20 segundos
    }
    /**
     * Generar soles
     * @return cantidad de soles generados
     */
    
    public int getTotalProduction() {
        return sunProduction * (health / 100);
    }
}