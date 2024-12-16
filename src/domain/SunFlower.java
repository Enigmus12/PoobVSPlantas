package domain;

import javax.swing.Timer;

public class SunFlower extends Plant {
    private static final int SUN_PRODUCTION = 25;  // Cantidad de soles generados
    private static final int PRODUCTION_TIME_MS = 20 * 1000;  
    private static final int MIN_HEALTH = 0;  // Salud mínima (muerta)

    protected long lastSunTime; // Última vez que generó soles
    private Timer sunProductionTimer;

    public SunFlower(int row, int col) {
        this.health = 300;
        this.sunCost = 50;
        this.positionX = row;
        this.positionY = col;
        this.lastSunTime = System.currentTimeMillis();  // Inicializar la última vez que generó soles
        
        // Iniciar el timer para producción de soles
        initializeSunProductionTimer();
    }

    // Inicializar el timer para la producción de soles
    private void initializeSunProductionTimer() {
        sunProductionTimer = new Timer(PRODUCTION_TIME_MS, e -> produceSuns());
        sunProductionTimer.start();
    }

    // Método para producir soles
    public void produceSuns() {
        long currentTime = System.currentTimeMillis();
        
        // Verificar si han pasado 20 segundos desde la última producción
        if (currentTime - lastSunTime >= PRODUCTION_TIME_MS && isAlive()) {
            // Obtener la instancia del tablero (usando Singleton)
            Board board = Board.getBoard();
            board.addSuns(SUN_PRODUCTION);
            
            // Actualizar la última vez que se generaron soles
            lastSunTime = currentTime;
        }
    }

    // Método para verificar si la planta está viva
    public boolean isAlive() {
        return this.health > MIN_HEALTH;
    }

    // Método para detener la producción de soles r
    public void stopSunProduction() {
        if (sunProductionTimer != null) {
            sunProductionTimer.stop();
        }
    }

    public void setHealth(int health) {
        this.health = health;
        
        // Si la salud llega a 0 o menos, detener la producción de soles
        if (health <= MIN_HEALTH) {
            stopSunProduction();
        }
    }

    public int getHealth() {
        return this.health;
    }
}