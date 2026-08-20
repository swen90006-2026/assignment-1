package swen90006.pfms;

public class UnauthenticatedAccountException extends Exception {
    public UnauthenticatedAccountException(String plateNumber) {
        super("Neither the account " + plateNumber + " nor the authority is currently authenticated.");
    }
}
