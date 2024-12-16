package domain;

public class EciPlant extends SunFlower {
    private static final int ECI_SUN_PRODUCTION = 50; 
    private static final int ECI_PRODUCTION_TIME_MS = 20 * 1000;  
    private static final int ECI_MIN_HEALTH = 0;  

    public EciPlant(int row, int col) {
        super(row, col);

        this.health = 150; 
        this.sunCost = 75;  
    }

    @Override
    public void produceSuns() {
        long currentTime = System.currentTimeMillis();
        
        // Verificar si han pasado 20 segundos desde la última producción
        if (currentTime - lastSunTime >= ECI_PRODUCTION_TIME_MS && isAlive()) {
            // Obtener la instancia del tablero (usando Singleton)
            Board board = Board.getBoard();
            board.addSuns(ECI_SUN_PRODUCTION);
            
            // Actualizar la última vez que se generaron soles
            lastSunTime = currentTime;
        }
    }

    @Override
    public boolean isAlive() {
        return this.health > ECI_MIN_HEALTH;
    }
}