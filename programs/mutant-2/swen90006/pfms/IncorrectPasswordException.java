package swen90006.pfms;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String username, String password) {
        super("Incorrect password: " + password + " for account " + username);
    }
}
