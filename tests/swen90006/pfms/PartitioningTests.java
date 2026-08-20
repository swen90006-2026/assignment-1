package swen90006.pfms;

import org.junit.*;

import static org.junit.Assert.*;

public class PartitioningTests {
    // The PFMS instance variable pfms is shared across all test methods in this class
    protected PFMS pfms;

     /**
     * The setup method annotated with "@Before" runs before each test.
     * By default, it initializes the PFMS instance and registers a dummy vehicle owner.
     * Use this method to set up any common test data or state.
     */

    @Before
    public void setUp() throws DuplicateAccountException, InvalidUsernameException, InvalidPasswordException {
        pfms = new PFMS();
        pfms.registerAccount("1ABC234", "Password1!", PFMS.Role.VEHICLE_OWNER);

    }

     /**
     * The teardown method annotated with "@After" runs after each test.
     * It's useful for cleaning up resources or resetting states.
     * Currently, this method doesn't perform any actions, but you can customize it as needed.
     */
    @After
    public void tearDown() {
        // No resources to clean up in this example, but this is where you would do so if needed
    }

    /**
     * This is a basic example test annotated with "@Test" to demonstrate how to use assertions in JUnit.
     * The assertEquals method checks if the expected value matches the actual value.
     */

    @Test
    public void aTest(){
        final int expected = 2;
        final int actual = 1 + 1;
        // Use of assertEquals to verify that the expected value matches the actual value
        assertEquals(expected, actual);
    }

    /**
     * This test checks if the InvalidUsernameException is correctly thrown when registering
     * a VEHICLE_OWNER account with an invalid (too short) plate number.
     * The expected exception is specified in the @Test annotation.
     */
    @Test(expected = InvalidUsernameException.class)
    public void anExceptionTest()
            throws DuplicateAccountException, InvalidUsernameException, InvalidPasswordException {
        // Test registration with an invalid plate number
        // to test whether the appropriate exception is thrown.
        pfms.registerAccount("ab", "Password1!", PFMS.Role.VEHICLE_OWNER);
    }

     /**
     * This is an example of a test that is designed to fail.
     * It shows how to include an error message to provide feedback when a test doesn't pass.
     */
    @Test
    public void aFailedTest() {
        // This test currently fails to demonstrate how JUnit reports errors
        final int expected = 2;
        final int actual = 1 + 2;
        // Uncomment the following line to observe a test failure.
        assertEquals("Some failure message", expected, actual);
    }

    // ADD YOUR TESTS HERE
    // This is the section where you will add your own tests.
    // Follow the examples above to create your tests.

}
