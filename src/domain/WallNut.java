package domain;

public class WallNut extends Plant{

    public WallNut(int row, int col) {
        this.sunCost = 50;
        this.health = 4000;
        this.positionX = row;
        this.positionY = col;
    }

}