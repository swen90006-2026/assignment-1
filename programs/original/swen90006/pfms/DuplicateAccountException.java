package swen90006.pfms;

public class DuplicateAccountException extends Exception {
    public DuplicateAccountException(String plateNumber) {
        super("Account is already registered: " + plateNumber);
    }
}
