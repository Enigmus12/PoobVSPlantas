package domain;

import java.util.ArrayList;

public abstract class Machine {
    private static final ArrayList<String> listaPlants = new ArrayList<>();

    // Bloque estático para inicializar la lista
    static {
        listaPlants.add("Sunflower");
        listaPlants.add("Peashooter");
        listaPlants.add("WallNut");
        listaPlants.add("PotatoMine");
        listaPlants.add("EciPlant");
    }

    // Atributo estático para la lista de nombres de zombies
    public static final ArrayList<String> listaZombies = new ArrayList<>();

    // Bloque estático para inicializar la lista
    static {
        listaZombies.add("ZombieBasic");
        listaZombies.add("ZombieConehead");
        listaZombies.add("ZombieBuckethead");
    }

}
