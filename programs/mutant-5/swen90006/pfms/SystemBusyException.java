package swen90006.pfms;

public class SystemBusyException extends Exception {
    public SystemBusyException(String attemptedUsername) {
        super("Could not log in " + attemptedUsername +
                ": another account is currently logged in. Please try again later.");
    }
}
