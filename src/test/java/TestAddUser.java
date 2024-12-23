import Model.Utilisateur;
import Util.DatabaseConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestAddUser {

    private String username = "testUser";
    private String password = "testPassword";
    private String email = "test@example.com";
    private String role = "benevolat";
    private String age = "25";

    @Before
    public void setUp() {
        DatabaseConnection.checkAndCreateTable(); // Ensure the table is created
        DatabaseConnection.deleteUserFromDatabase(username); // Remove the user if exists before each test
    }

    @Test
    public void testSignInButton() {
        Utilisateur newUser = new Utilisateur(username, password, email, role, age);
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.addUtilisateur(newUser);
        
        // Check if the user was added successfully
        boolean isUserAdded = DatabaseConnection.isUserInDatabase(username);
        assertTrue("User should be added to the database", isUserAdded);
    }

    @After
    public void tearDown() {
        DatabaseConnection.deleteUserFromDatabase(username); // Clean up after the test
    }
}
