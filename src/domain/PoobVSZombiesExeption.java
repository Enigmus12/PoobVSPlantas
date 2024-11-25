package domain;

public class PoobVSZombiesExeption extends Exception{

    public static final String INVALID_NAME = "Digite Un Nombre";
    public static final String INCORRECT_POSITION = "No es posible plantar en esta celda";
    public static final String CELL_IS_OCUPATED = "En esta celda ya existe una planta";
    public static final String INVALID_PLANT= "Tipo de planta no valida";
    public static final String INSUFFICIENT_SUNS ="Soles insuficientes";
    public static final String CELL_IS_EMPTY ="En esta celda no hay plantas";

    public PoobVSZombiesExeption(String message){
        super(message);
    }

}