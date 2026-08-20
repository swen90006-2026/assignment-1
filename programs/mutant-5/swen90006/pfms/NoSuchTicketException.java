package swen90006.pfms;

public class NoSuchTicketException extends Exception {
    public NoSuchTicketException(String ticketNumber, String plateNumber) {
        super(plateNumber == null
                ? "No such ticket found with ticket number: " + ticketNumber
                : "No ticket with ticket number " + ticketNumber + " found for account " + plateNumber);
    }
}
