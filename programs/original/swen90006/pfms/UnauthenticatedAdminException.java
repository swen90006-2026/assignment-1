package swen90006.pfms;

public class UnauthenticatedAdminException extends Exception {
    public UnauthenticatedAdminException() {
        super("No admin account is currently authenticated.");
    }
}
