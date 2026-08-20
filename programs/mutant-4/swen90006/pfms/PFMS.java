package swen90006.pfms;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class PFMS {
    /**
     * The minimum length of a vehicle owner's username (plate number)
     */
    public final static int MINIMUM_PLATE_LENGTH = 6;

    /**
     * The maximum length of a vehicle owner's username (plate number)
     */
    public final static int MAXIMUM_PLATE_LENGTH = 8;

    /**
     * The minimum length of an admin's username
     */
    public final static int MINIMUM_ADMIN_USERNAME_LENGTH = 4;

    /**
     * The maximum length of an admin's username
     */
    public final static int MAXIMUM_ADMIN_USERNAME_LENGTH = 12;

    /**
     * The minimum length of a password
     */
    public final static int MINIMUM_PASSWORD_LENGTH = 10;

    /**
     * The maximum length of a password
     */
    public final static int MAXIMUM_PASSWORD_LENGTH = 16;

    /**
     * The role held by an account: a vehicle owner, or a council admin.
     */
    public enum Role {VEHICLE_OWNER, ADMIN}

    /**
     * The type of parking violation a ticket is issued for. Each fine type carries a fixed,
     * system-defined fine amount; the amount is not supplied by the caller of issueTicket.
     */
    public enum FineType {
        OVERSTAYED_PARKING(65.0),
        NO_STANDING_ZONE(90.0),
        EXPIRED_METER(55.0),
        DISABLED_PARKING_MISUSE(300.0);

        private final double fineAmount;

        FineType(double fineAmount) {
            this.fineAmount = fineAmount;
        }

        public double getFineAmount() {
            return fineAmount;
        }
    }

    /**
     * Stores the password for each registered account, keyed by username (a plate number for
     * VEHICLE_OWNER accounts, or an admin username for ADMIN accounts).
     */
    private Map<String, String> passwords;

    /**
     * Stores the role (VEHICLE_OWNER or ADMIN) for each registered account, keyed by username.
     */
    private Map<String, Role> roles;

    /**
     * The system supports at most one active (logged-in) user at any time, regardless of role.
     * This field holds that user's username, or {@code null} if nobody is currently logged in.
     * An account must log out (see {@link #logout}) before a different account can log in.
     */
    private String activeUsername;

    /**
     * Stores all tickets ever issued in the system, in the order they were issued.
     */
    private List<Ticket> tickets;

    /**
     * Counter used to generate the next unique ticket number.
     */
    private int nextTicketNumber;

    /**
     * Represents a single issued parking fine ticket.
     * Instances can only be created and updated by the enclosing PFMS class.
     */
    public static class Ticket {
        private final String ticketID;
        private final String plateNumber;
        private final FineType fineType;
        private final double fineAmount;
        private double amountPaid;

        private Ticket(String ticketID, String plateNumber, FineType fineType, double fineAmount, double amountPaid) {
            this.ticketID = ticketID;
            this.plateNumber = plateNumber;
            this.fineType = fineType;
            this.fineAmount = fineAmount;
            this.amountPaid = amountPaid;
        }

        public String getTicketID() {
            return ticketID;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public FineType getFineType() {
            return fineType;
        }

        public double getFineAmount() {
            return fineAmount;
        }

        public double getAmountPaid() {
            return amountPaid;
        }

        private void addPayment(double amount) {
            this.amountPaid += amount;
        }
    }

    /**
     * Constructs a new Parking Fine Management System (PFMS) instance with no registered
     * accounts, no issued tickets, and no active (logged-in) user.
     */
    public PFMS() {
        passwords = new HashMap<>();
        roles = new HashMap<>();
        activeUsername = null;
        tickets = new ArrayList<>();
        nextTicketNumber = 1;
    }

    /**
     * Validates whether the given username meets the specified criteria for a VEHICLE_OWNER
     * account (i.e. a plate number).
     *
     * <p>The username must satisfy the following conditions:
     * <ul>
     *   <li>It must be between 6 and 8 characters long (inclusive).</li>
     *   <li>It must contain only upper-case letters (A-Z) and digits (0-9).</li>
     *   <li>It must contain at least one letter and at least one digit.</li>
     * </ul>
     *
     * @param username the username to be validated
     * @return {@code true} if the username is a valid plate number; {@code false} otherwise
     */
    private boolean isValidPlateNumber(String username) {
        if (username.length() < MINIMUM_PLATE_LENGTH || username.length() > MAXIMUM_PLATE_LENGTH) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : username.toCharArray()) {
            if ('A' <= c && c <= 'Z') {
                hasLetter = true;
            } else if ('0' <= c && c <= '9') {
                hasDigit = true;
            } else {
                return false;
            }
        }

        return hasLetter && hasDigit;
    }

    /**
     * Validates whether the given username meets the specified criteria for an ADMIN account.
     *
     * <p>The username must satisfy the following conditions:
     * <ul>
     *   <li>It must be between 4 and 12 characters long (inclusive).</li>
     *   <li>It must contain only lower- and upper-case letters (a-z, A-Z).</li>
     * </ul>
     *
     * @param username the username to be validated
     * @return {@code true} if the username is a valid admin username; {@code false} otherwise
     */
    private boolean isValidAdminUsername(String username) {
        if (username.length() < MINIMUM_ADMIN_USERNAME_LENGTH || username.length() > MAXIMUM_ADMIN_USERNAME_LENGTH) {
            return false;
        }

        for (char c : username.toCharArray()) {
            if (!('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z')) {
                return false;
            }
        }

        return true;
    }

    /**
     * Registers a new account with an authentication status of NOT_AUTHENTICATED, with the
     * given role.
     *
     * <h3>Requirements:</h3>
     *
     * <ul>
     *   <li><strong>Username:</strong>
     *     <ul>
     *       <li>Must not have been registered previously.</li>
     *       <li>If role is VEHICLE_OWNER: must be a valid plate number (see
     *           {@link #isValidPlateNumber}).</li>
     *       <li>If role is ADMIN: must be a valid admin username (see
     *           {@link #isValidAdminUsername}).</li>
     *     </ul>
     *   </li>
     *   <li><strong>Password:</strong>
     *     <ul>
     *       <li>Must be between 10 and 16 characters long (inclusive).</li>
     *       <li>Must contain at least one letter (a-z, A-Z).</li>
     *       <li>Must contain at least one digit (0-9).</li>
     *       <li>Must contain at least one special character (anything other than a-z, A-Z, or 0-9)</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <h3>Exceptions (thrown in order):</h3>
     * <ul>
     *   <li><strong>DuplicateAccountException:</strong> Thrown if the username is already registered.</li>
     *   <li><strong>InvalidUsernameException:</strong> Thrown if the username does not meet the role-specific requirements.</li>
     *   <li><strong>InvalidPasswordException:</strong> Thrown if the password does not meet the specified requirements.</li>
     * </ul>
     *
     * @param username The username for the account to be registered.
     * @param password The password for the account.
     * @param role     The role of the account being registered (VEHICLE_OWNER or ADMIN).
     * @throws DuplicateAccountException Thrown if the username is already registered.
     * @throws InvalidUsernameException  Thrown if the username does not meet the role-specific requirements.
     * @throws InvalidPasswordException  Thrown if the password does not meet the specified requirements.
     */
    public void registerAccount(String username, String password, Role role)
            throws DuplicateAccountException, InvalidUsernameException, InvalidPasswordException {

        if (passwords.containsKey(username)) {
            throw new DuplicateAccountException(username);
        }

        boolean validUsername = (role == Role.VEHICLE_OWNER)
                ? isValidPlateNumber(username)
                : isValidAdminUsername(username);

        if (!validUsername) {
            throw new InvalidUsernameException(username, role);
        } else {
            boolean hasLetter = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;
            if (password.length() < MINIMUM_PASSWORD_LENGTH || password.length() > MAXIMUM_PASSWORD_LENGTH) {
                throw new InvalidPasswordException(password);
            } else {
                for (char c : password.toCharArray()) {
                    if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z') {
                        hasLetter = true;
                    } else if ('0' <= c && c <= '9') {
                        hasDigit = true;
                    } else {
                        hasSpecial = true;
                    }
                }

                if (!(hasLetter && hasDigit && hasSpecial)) {
                    throw new InvalidPasswordException(password);
                } else {
                    passwords.put(username, password);
                    roles.put(username, role);
                }
            }
        }
    }

    /**
     * Authenticates an account using a username and password, and makes it the system's
     * single active (logged-in) user. The username may identify either a VEHICLE_OWNER
     * account (a plate number) or an ADMIN account; login behaves identically for both roles.
     *
     * <p>The system supports at most one active user at a time, regardless of role. If any
     * account (including this same account) is already logged in, this call fails immediately
     * with SystemBusyException; the account attempting to log in must wait for the current
     * active user to log out (see {@link #logout}) and try again.
     *
     * @param username The username of the account being logged into.
     * @param password The password associated with the account being logged into.
     * @throws SystemBusyException        If another account is already logged in.
     * @throws NoSuchAccountException     If username is not registered.
     * @throws IncorrectPasswordException If the password does not match the account identified by username.
     */
    public void login(String username, String password)
            throws SystemBusyException, NoSuchAccountException, IncorrectPasswordException {

        if (activeUsername != null) {
            throw new SystemBusyException(username);
        }

        if (!passwords.containsKey(username)) {
            throw new NoSuchAccountException(username);
        }

        String storedPassword = passwords.get(username);
        if (!storedPassword.equals(password)) {
            throw new IncorrectPasswordException(username, password);
        }

        activeUsername = username;
    }

    /**
     * Logs out the system's current active user, freeing up the single active-session slot so
     * that a different account may subsequently log in.
     *
     * @throws NoActiveSessionException If no account is currently logged in.
     */
    public void logout() throws NoActiveSessionException {
        if (activeUsername == null) {
            throw new NoActiveSessionException();
        }
        activeUsername = null;
    }

    /**
     * Allows a user to retrieve the list of outstanding tickets for a given plateNumber. Can
     * be used both by the vehicle owner (for their own plateNumber) and by any admin (for any
     * vehicle owner's plateNumber), provided the relevant account is currently authenticated.
     *
     * @param plateNumber The plate number whose outstanding tickets are being requested.
     * @return A list of outstanding tickets for plateNumber, in issuance order.
     * @throws NoSuchAccountException          If plateNumber is not registered.
     * @throws IncorrectAccountRoleException   If plateNumber is registered but is not a VEHICLE_OWNER account.
     * @throws UnauthenticatedAccountException If neither plateNumber's owner nor any admin is currently authenticated.
     */
    public List<Ticket> checkOutstandingTickets(String plateNumber)
            throws NoSuchAccountException, IncorrectAccountRoleException, UnauthenticatedAccountException {

        if (!isRegistered(plateNumber)) {
            throw new NoSuchAccountException(plateNumber);
        }

        if (getRole(plateNumber) != Role.VEHICLE_OWNER) {
            throw new IncorrectAccountRoleException(plateNumber, Role.VEHICLE_OWNER);
        }

        if (!isAuthenticated(plateNumber) && !isAnyAdminAuthenticated()) {
            throw new UnauthenticatedAccountException(plateNumber);
        }

        List<Ticket> outstanding = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPlateNumber().equals(plateNumber) && t.getFineAmount() > t.getAmountPaid()) {
                outstanding.add(t);
            }
        }
        return outstanding;
    }

    /**
     * Allows any authenticated admin to issue a new parking fine ticket against a registered
     * vehicle owner, for a given type of parking violation. The fine amount is determined by
     * fineType and is not supplied by the caller.
     *
     * @param plateNumber The plate number of the vehicle being fined.
     * @param fineType    The type of parking violation being fined.
     * @return The system-generated ticket number for the newly issued ticket.
     * @throws NoSuchAccountException        If plateNumber is not registered.
     * @throws IncorrectAccountRoleException If plateNumber is registered but is not a VEHICLE_OWNER account.
     * @throws UnauthenticatedAdminException If no admin account is currently authenticated.
     */
    public String issueTicket(String plateNumber, FineType fineType)
            throws NoSuchAccountException, IncorrectAccountRoleException, UnauthenticatedAdminException {

        if (!isRegistered(plateNumber)) {
            throw new NoSuchAccountException(plateNumber);
        }

        if (getRole(plateNumber) != Role.VEHICLE_OWNER) {
            throw new IncorrectAccountRoleException(plateNumber, Role.VEHICLE_OWNER);
        }

        if (!isAnyAdminAuthenticated()) {
            throw new UnauthenticatedAdminException();
        }

        String ticketNumber = "T" + nextTicketNumber;
        nextTicketNumber++;
        tickets.add(new Ticket(ticketNumber, plateNumber, fineType, fineType.getFineAmount(), 0.0));
        return ticketNumber;
    }

    /**
     * Allows a payment to be made towards one specific ticket belonging to a plateNumber,
     * identified by its ticketNumber.
     *
     * @param plateNumber  The plate number the payment is being made against.
     * @param ticketNumber The ticket number of the specific ticket being paid.
     * @param amount       The dollar amount being paid; must be strictly greater than 0.
     * @throws NoSuchAccountException        If plateNumber is not registered.
     * @throws IncorrectAccountRoleException If plateNumber is registered but is not a VEHICLE_OWNER account.
     * @throws NoSuchTicketException         If ticketNumber does not identify a ticket belonging to plateNumber.
     * @throws InvalidPaymentAmountException If amount is not strictly greater than 0.
     * @throws NoOutstandingTicketException  If the identified ticket has already been fully paid.
     * @throws OverpaymentException          If amount is strictly greater than the identified ticket's outstanding balance.
     */
    public void payTicket(String plateNumber, String ticketNumber, double amount)
            throws NoSuchAccountException, IncorrectAccountRoleException, NoSuchTicketException,
            InvalidPaymentAmountException, NoOutstandingTicketException, OverpaymentException {

        if (!isRegistered(plateNumber)) {
            throw new NoSuchAccountException(plateNumber);
        }

        if (getRole(plateNumber) != Role.VEHICLE_OWNER) {
            throw new IncorrectAccountRoleException(plateNumber, Role.VEHICLE_OWNER);
        }

        Ticket ticket = null;
        for (Ticket t : tickets) {
            if (t.getTicketID().equals(ticketNumber) && t.getPlateNumber().equals(plateNumber)) {
                ticket = t;
                break;
            }
        }
        if (ticket == null) {
            throw new NoSuchTicketException(ticketNumber, plateNumber);
        }

        if (amount <= 0) {
            throw new InvalidPaymentAmountException(amount);
        }

        double outstandingBalance = ticket.getFineAmount() - ticket.getAmountPaid();
        if (outstandingBalance <= 0) {
            throw new NoOutstandingTicketException(plateNumber);
        }

        if (amount > outstandingBalance) {
            throw new OverpaymentException(plateNumber, amount, outstandingBalance);
        }

        ticket.addPayment(amount);
    }


    /**
     * Below are some helper functions for you to use in your tests.
     * You are not required to use all these in your tests, but they can certainly help you.
     * You **should not** add any more helper functions here.
     */


    /**
     * Checks if the account with the given username is registered.
     * @param username The username to check.
     * @return {@code true} if the account is registered
     * @throws NoSuchAccountException if the username does not exist in the system
     */
    public boolean isRegistered(String username)
            throws NoSuchAccountException
    {
        if (!passwords.containsKey(username)) {
            throw new NoSuchAccountException(username);
        }
        return true;
    }

    /**
     * Checks if the account with the given username is the system's current active
     * (logged-in) user.
     * @param username The username to check.
     * @return {@code true} if username is currently the active user, false otherwise
     * @throws NoSuchAccountException if the username does not exist in the system
     */
    public boolean isAuthenticated(String username)
            throws NoSuchAccountException
    {
        if (!passwords.containsKey(username)) {
            throw new NoSuchAccountException(username);
        }
        return username.equals(activeUsername);
    }

    /**
     * Returns the role of the account with the given username.
     * @param username The username to check.
     * @return The Role of the account (VEHICLE_OWNER or ADMIN).
     * @throws NoSuchAccountException if the username does not exist in the system
     */
    public Role getRole(String username) throws NoSuchAccountException {
        if (!roles.containsKey(username)) {
            throw new NoSuchAccountException(username);
        }
        return roles.get(username);
    }

    /**
     * Returns the username of the system's current active (logged-in) user.
     * @return The active user's username, or {@code null} if nobody is currently logged in.
     */
    public String getActiveUsername() {
        return activeUsername;
    }

    /**
     * Checks if the system's current active (logged-in) user, if any, is an ADMIN.
     * @return {@code true} if the active user exists and has role ADMIN, false otherwise.
     */
    public boolean isAnyAdminAuthenticated() {
        return activeUsername != null && roles.get(activeUsername) == Role.ADMIN;
    }

    /**
     * Returns the total outstanding balance for a plateNumber, i.e. the sum of
     * (fineAmount - amountPaid) across all of that plateNumber's tickets.
     * @param plateNumber The plate number to check.
     * @return The total outstanding balance for plateNumber.
     * @throws NoSuchAccountException if the plateNumber does not exist in the system
     */
    public double getOutstandingBalance(String plateNumber)
            throws NoSuchAccountException
    {
        if (!passwords.containsKey(plateNumber)) {
            throw new NoSuchAccountException(plateNumber);
        }
        double total = 0.0;
        for (Ticket t : tickets) {
            if (t.getPlateNumber().equals(plateNumber)) {
                total += (t.getFineAmount() - t.getAmountPaid());
            }
        }
        return total;
    }

    /**
     * Checks if a plateNumber has any outstanding tickets.
     * @param plateNumber The plate number to check.
     * @return {@code true} if plateNumber has at least one outstanding ticket, false otherwise.
     * @throws NoSuchAccountException if the plateNumber does not exist in the system
     */
    public boolean hasOutstandingTickets(String plateNumber)
            throws NoSuchAccountException
    {
        return getOutstandingBalance(plateNumber) > 0;
    }

    /**
     * Returns the total number of tickets ever issued to a plateNumber (outstanding or paid).
     * @param plateNumber The plate number to check.
     * @return The number of tickets issued to plateNumber.
     */
    public int getTicketCount(String plateNumber) {
        int count = 0;
        for (Ticket t : tickets) {
            if (t.getPlateNumber().equals(plateNumber)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the ticket identified by the given ticketNumber.
     * @param ticketNumber The ticket number to look up.
     * @return The Ticket identified by ticketNumber.
     * @throws NoSuchTicketException if no ticket with that ticketNumber exists in the system.
     */
    public Ticket getTicket(String ticketNumber) throws NoSuchTicketException {
        for (Ticket t : tickets) {
            if (t.getTicketID().equals(ticketNumber)) {
                return t;
            }
        }
        throw new NoSuchTicketException(ticketNumber, null);
    }

    /**
     * Checks if a ticket matching the given plateNumber, fineType and amountPaid exists in
     * the system.
     * @param plateNumber The plate number to check.
     * @param fineType    The fine type to check.
     * @param amountPaid  The amount paid to check.
     * @return {@code true} if a matching ticket is found, {@code false} otherwise.
     */
    public boolean isSavedTicket(String plateNumber, FineType fineType, double amountPaid) {
        for (Ticket t : tickets) {
            if (t.getPlateNumber().equals(plateNumber)
                    && t.getFineType() == fineType
                    && t.getAmountPaid() == amountPaid) {
                return true;
            }
        }
        return false;
    }

}
