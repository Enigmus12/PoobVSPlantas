package domain;

public abstract class Plant extends Character {
    protected int rechargeTime;
    protected int sunCost;
    protected int health;
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

    @Override
    public int getHealth() {
        return health;
    }

    public int getSunCost() {
        return sunCost;
    }

    @Override
    public void receiveDamage(int amount) {
        super.receiveDamage(amount);
        
    }
}