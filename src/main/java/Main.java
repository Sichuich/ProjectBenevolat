
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Controller.HomeBenevolat;
import Controller.HomeValidateur;
import Controller.Homehospitalise;
import Model.Utilisateur;
import Util.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.checkAndCreateTable();
        DatabaseConnection.checkAndCreateDemandeTable();

        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Authentification Utilisateur");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 150));
        frame.add(panel);

        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, final JFrame frame) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Nom d'utilisateur:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        nameLabel.setForeground(Color.white);
        panel.add(nameLabel, constraints);

        final JTextField nameText = new JTextField(20);
        constraints.gridx = 1;
        panel.add(nameText, constraints);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        passwordLabel.setForeground(Color.white);
        panel.add(passwordLabel, constraints);

        final JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Se connecter");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        loginButton.setBackground(new Color(200, 200, 200));
        loginButton.setForeground(new Color(50, 50, 150));
        panel.add(loginButton, constraints);

        JButton inscribeButton = new JButton("S'inscrire");
        constraints.gridy = 3;
        panel.add(inscribeButton, constraints);

        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> {
            String username = nameText.getText();
            String password = new String(passwordField.getPassword());

            Utilisateur usertest = DatabaseConnection.authenticate(username, password);

            if (usertest != null) {
                JOptionPane.showMessageDialog(null, "Authentification réussie!");
                frame.setVisible(false);

                switch (usertest.getRole()) {
                    case "benevolat":
                        new HomeBenevolat(usertest);
                        break;
                    case "validateur":
                        new HomeValidateur(usertest);
                        break;
                    case "hospitalise":
                        new Homehospitalise(usertest);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rôle non reconnu. Veuillez réessayer.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Authentification échouée. Veuillez réessayer.");
            }
        });

        inscribeButton.addActionListener(e -> testSignInButton(frame));
    }

    private static void testSignInButton(JFrame frame) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID (3 numbers):"));
            String username = JOptionPane.showInputDialog("Enter username:");
            String password = JOptionPane.showInputDialog("Enter password:");
            String email = JOptionPane.showInputDialog("Enter email:");

            String[] roles = {"benevolat", "validateur", "hospitalise"};
            String role = (String) JOptionPane.showInputDialog(null, "Choose a role:", "Select Role",
                    JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

            String age = JOptionPane.showInputDialog("Enter age:");

            Utilisateur newUser = new Utilisateur(username, password, email, role, age, id);

            DatabaseConnection.addUtilisateur(newUser);
            JOptionPane.showMessageDialog(frame, "Inscription réussie !");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "ID invalide. Veuillez entrer un nombre !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'inscription : " + ex.getMessage());
        }
    }
}
