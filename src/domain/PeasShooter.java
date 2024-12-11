package domain;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class PeasShooter extends Plant implements IAttacker {

    public PeasShooter(int row, int col) {
        this.health = 100;
        this.sunCost = 100;
        this.positionX = row;
        this.positionY = col;
        attack();
    }

    @Override
    public void attack() {
        //falta implementar
    }


}
