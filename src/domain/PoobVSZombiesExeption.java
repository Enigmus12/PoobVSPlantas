package domain;

public class PoobVSZombiesExeption extends Exception{

    public static final String INVALID_NAME = "Digite Un Nombre";
    public static final String INCORRECT_POSITION = "Posicion incorrecta";
    public static final String CELL_IS_OCUPATED = "En esta celda ya existe una planta";
    public static final String NO_ENOUGH_SUNS = "No tienes suficientes soles";

    public PoobVSZombiesExeption(String message){
        super(message);
    }

}