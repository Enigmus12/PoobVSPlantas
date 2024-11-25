package domain;

public class WallNut extends Plant implements IDefender {
    private int resistance;

    public WallNut(int row, int col) {
        this.sunCost = 50;
        this.resistance = 4000;
        this.positionX = row;
        this.positionY = col;
    }

    @Override
    public void defend() {
    }

}