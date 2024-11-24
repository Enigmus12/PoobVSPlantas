package domain;

public class PoobVSZombiesExeption extends Exception{

    public static final String INVALID_NAME = "Digite Un Nombre";
    public static final String INCORRECT_POSITION = "Posicion incorrecta";
    public PoobVSZombiesExeption(String message){
        super(message);
    }

}
