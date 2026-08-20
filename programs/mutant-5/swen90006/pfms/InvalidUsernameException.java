package swen90006.pfms;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String username, PFMS.Role role) {
        super("Username \"" + username + "\" does not comply with the requirements for role "
                + role + ":\n" + requirementsFor(role));
    }

    private static String requirementsFor(PFMS.Role role) {
        if (role == PFMS.Role.VEHICLE_OWNER) {
            return "\t- must be between " + PFMS.MINIMUM_PLATE_LENGTH + " and " +
                    PFMS.MAXIMUM_PLATE_LENGTH + " characters long,\n" +
                    "\t- must contain only upper-case letters (A-Z) and digits (0-9),\n" +
                    "\t- must contain at least one letter and at least one digit.";
        } else {
            return "\t- must be between " + PFMS.MINIMUM_ADMIN_USERNAME_LENGTH + " and " +
                    PFMS.MAXIMUM_ADMIN_USERNAME_LENGTH + " characters long,\n" +
                    "\t- must contain only letters (a-z, A-Z).";
        }
    }
}
