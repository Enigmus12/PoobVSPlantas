package domain;

public class Sunflower extends Plant {
    private int sunProduction;
    private int productionTime;
    
    public Sunflower() {
        this.health = 300;
        this.sunCost = 50;
        this.sunProduction = 25;
        this.productionTime = 20; // segundos
        this.rechargeTime = 7; // segundos
    }
    
    /**
     * Generar soles
     * @return cantidad de soles generados
     */
    public int generateSuns() {
        return sunProduction;
    }
    
    public int getTotalProduction() {
        return sunProduction * (health / 100);
    }
}
