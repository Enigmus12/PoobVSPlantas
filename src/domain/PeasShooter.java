package domain;

public class PeasShooter extends Plant implements IAttacker {

    
    public PeasShooter(int row, int col) {
        this.health = 100;
        this.sunCost = 100;
        this.positionX = row;
        this.positionY = col; 
    }
    
    @Override
    public void attack() {
    }

    

}
