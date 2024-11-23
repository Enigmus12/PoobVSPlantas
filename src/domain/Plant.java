package domain;

public abstract class Plant extends Character {
    protected int sunCost;
    
    /**
     * Verificar si la planta puede ser plantada
     * @return true si cumple las condiciones para ser plantada
     */
    public boolean canBePlanted() {
        return true;
    }
    
    public int getSunCost() {
        return sunCost;
    }
    
}
