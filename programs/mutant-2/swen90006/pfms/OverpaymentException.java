package swen90006.pfms;

public class OverpaymentException extends Exception {
    public OverpaymentException(String plateNumber, double amount, double outstandingBalance) {
        super("Payment " + amount + " for account " + plateNumber +
                " exceeds outstanding balance " + outstandingBalance + ".");
    }
}
