package domain;

public class ConeheadZombie extends Zombie {
    private int coneResistance;
    
    public ConeheadZombie(int row,int column) {
        super(row,column);

        this.health = 100;
        this.speed = (int) 1.0f;
        this.coneResistance = 280;
    }
    
    
    public void loseCone() {
        this.coneResistance = 0;
    }
    

}
