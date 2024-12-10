package domain;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class PeasShooter extends Plant implements IAttacker {
    private List<Pea> peas;  // Lista para almacenar los guisantes disparados
    private Timer peaTimer;  // Temporizador para disparar guisantes
    private static final int PEA_SHOOTING_INTERVAL = 5000; // Igual al de GameCell
    private static final int PEA_MOVEMENT_INTERVAL = 50;   // Igual al de GameCell

    public PeasShooter(int row, int col) {
        this.health = 100;
        this.sunCost = 100;
        this.positionX = row;
        this.positionY = col;
        this.peas = new ArrayList<>();
        attack();
    }

    @Override
    public void attack() {
        peaTimer = new Timer(PEA_SHOOTING_INTERVAL, e -> shootPea());
        peaTimer.start();
    }

    private void shootPea() {
        Pea newPea = new Pea(positionX, positionY);
        peas.add(newPea);
        newPea.startMovement();
    }

    public void stopShooting() {
        if (peaTimer != null) {
            peaTimer.stop();
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            stopShooting();
            this.health = 0;  // La planta está destruida
        }
    }

    // Clase interna para manejar el movimiento de los guisantes
    private class Pea {
        private int x, y;  // Posición del guisante
        private Timer movePeaTimer;  // Temporizador para mover el guisante

        public Pea(int row, int col) {
            this.x = row;
            this.y = col;
        }

        public void startMovement() {
            movePeaTimer = new Timer(PEA_MOVEMENT_INTERVAL, e -> {
                y += 2;  // Mover el guisante hacia adelante
                if (collisionDetected()) {
                    stopMovement();
                }
            });
            movePeaTimer.start();
        }

        private boolean collisionDetected() {

            return false;  // Guisante salió del tablero
        }

        public void stopMovement() {
            if (movePeaTimer != null) {
                movePeaTimer.stop();
            }
        }
    }
}
