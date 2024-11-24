package domain;

public class GameController {

    public void validateNameOnePlayer(String name) throws PoobVSZombiesExeption {
        if (name == null || name.trim().isEmpty() || name.equals("Enter your name here") || !name.matches("[a-zA-Z ]+")) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME);
        }
    }

    public void validateNameTwoPlayers(String name, String player) throws PoobVSZombiesExeption {
        if (name == null || name.trim().isEmpty() || name.equals("Name player one") || name.equals("Name player two") || !name.matches("[a-zA-Z ]+")) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME + " " + player);
        }
    }
}

