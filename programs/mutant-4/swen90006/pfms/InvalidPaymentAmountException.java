package swen90006.pfms;

public class InvalidPaymentAmountException extends Exception {
    public InvalidPaymentAmountException(double amount) {
        super("Invalid payment amount: " + amount + ". Amount must be strictly greater than 0.");
    }
}
