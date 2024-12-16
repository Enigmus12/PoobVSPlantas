package domain;

public class BasicZombie extends Zombie {
    /**
     * Constructor de la clase `BasicZombie`.
     * Inicializa el zombie básico con una posición específica y valores predefinidos.
     *
     * @param row Fila en la que se posiciona el zombie.
     * @param col Columna en la que se posiciona el zombie.
     */
    public BasicZombie(int row, int col) {
        super(row, col); // Llama al constructor de la clase base `Zombie`
        this.health = 100; // Establece la salud inicial del zombie básico
        this.damage = 100; // Establece el daño que puede infligir el zombie básico
    }

}