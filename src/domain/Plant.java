package domain;

public abstract class Plant extends Character {
    protected int rechargeTime;
    protected int sunCost;
    
    /**
     * Verifica si la planta puede ser plantada
     * @return true si cumple las condiciones para ser plantada
     */
    public boolean canBePlanted(int row, int column) throws PoobVSZombiesExeption {
        if (column == 0 || column == 9) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
        return true;
    }
    
    public int getSunCost() {
        return sunCost;
    }
    
    @Override
    public void receiveDamage(int amount) {
        super.receiveDamage(amount);
        System.out.println("Plant at [" + positionX + "," + positionY + "] received " + amount + " damage. Remaining health: " + health);
    }
}