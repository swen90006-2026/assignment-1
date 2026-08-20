package swen90006.pfms;

public class NoOutstandingTicketException extends Exception {
    public NoOutstandingTicketException(String plateNumber) {
        super("Account " + plateNumber + " has no outstanding tickets.");
    }
}
