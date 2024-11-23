package domain;

public class Wallnut extends Plant implements IDefender {
    private int resistance;
    
    public Wallnut() {
        this.sunCost = 50;
        this.resistance = 4000;
    }
    
    @Override
    public void defend() {
    }
    
}
