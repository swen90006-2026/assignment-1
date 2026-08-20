package swen90006.pfms;

public class NoActiveSessionException extends Exception {
    public NoActiveSessionException() {
        super("No account is currently logged in.");
    }
}
