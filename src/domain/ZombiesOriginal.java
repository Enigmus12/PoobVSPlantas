package domain;

import java.util.HashMap;
import java.util.Random;

public class ZombiesOriginal extends Machine {


    public HashMap<String, int[]> attack() {
        Random random = new Random();

        // Seleccionar un zombie aleatorio de la lista pública
        String elegido = listaZombies.get(random.nextInt(listaZombies.size()));

        // Crear el HashMap para devolver
        HashMap<String, int[]> resultado = new HashMap<>();

        // Crear un ejemplo de valores aleatorios (puedes personalizarlos)
        int[] valores = {random.nextInt(5) , 9}; // Ejemplo: daño y rango

        // Agregar el zombie elegido y sus valores al HashMap
        resultado.put(elegido, valores);

        // Devolver el HashMap
        return resultado;
    }
}


