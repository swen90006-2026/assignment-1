package swen90006.pfms;

public class IncorrectAccountRoleException extends Exception {
    public IncorrectAccountRoleException(String username, PFMS.Role requiredRole) {
        super("Account " + username + " does not have the required role: " + requiredRole);
    }
}
