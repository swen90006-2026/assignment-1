package swen90006.pfms;

public class NoSuchAccountException extends Exception {
    public NoSuchAccountException(String username) {
        super("Account does not exist: " + username);
    }
}
