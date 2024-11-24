package domain;

public class PoobVSZombiesExeption extends Exception{

    public static final String INVALID_NAME = "Digite Un Nombre";

    public PoobVSZombiesExeption(String message){
        super(message);
    }

}
