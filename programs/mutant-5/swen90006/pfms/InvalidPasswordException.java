package swen90006.pfms;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String password) {
        super("Password \"" + password + "\" does not comply with the requirements:\n" +
                "\t- Must be between " + PFMS.MINIMUM_PASSWORD_LENGTH + " and " +
                     PFMS.MAXIMUM_PASSWORD_LENGTH + " characters long,\n" +
                "\t- Must contain at least one letter (a-z, A-Z),\n" +
                "\t- Must contain at least one digit (0-9),\n" +
                "\t- Must contain at least one special character (not a letter or digit).");
    }
}
