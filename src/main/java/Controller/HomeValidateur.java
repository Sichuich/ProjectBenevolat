package Controller;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Model.Utilisateur;

public class HomeValidateur extends JFrame {

    private static Utilisateur utilisateur;

    private static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_005";
    private static final String USER = "projet_gei_005";
    private static final String PASSWORD = "Sie2ooch";

    public HomeValidateur(Utilisateur utilisateur) {
        HomeValidateur.utilisateur = utilisateur;

        setTitle("Accueil Validateur");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel demandePanel = new JPanel(new GridLayout(0, 1));
        demandePanel.setBorder(BorderFactory.createTitledBorder("Liste des Demandes d'Aide"));
        demandePanel.setBackground(new Color(240, 240, 240));

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "SELECT iddemande, typeAide, description, demandeur, statut FROM demande";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int demandeId = resultSet.getInt("iddemande");
                    String typeAide = resultSet.getString("typeaide");
                    String description = resultSet.getString("description");
                    String demandeur = resultSet.getString("demandeur");
                    String statut = resultSet.getString("statut");

                    JPanel requestPanel = new JPanel(new GridLayout(1, 2));
                    requestPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                    requestPanel.setBackground(Color.white);

                    JLabel label = new JLabel("<html>Type: <b>" + typeAide + "</b>, Demandeur: <b>" + demandeur + "</b><br>Description: " + description + "</html>");
                    requestPanel.add(label);

                    JButton traiterButton = createStyledButton("Traiter", new Color(0, 128, 128));
                    traiterButton.addActionListener(e -> {
                        handleTraitement(demandeId, statut);
                        validate();
                        repaint();
                    });
                    requestPanel.add(traiterButton);

                    demandePanel.add(requestPanel);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        add(new JScrollPane(demandePanel));
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.white);
        return button;
    }

    private void handleTraitement(int demandeId, String statut) {
        if (statutDejaTraite(statut)) {
            JOptionPane.showMessageDialog(this, "Cette demande a déjà été traitée.");
            return;
        }

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choisissez une action pour cette demande:",
                "Traitement de la Demande",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Valider", "Refuser"},
                "Valider"
        );

        if (choice == JOptionPane.YES_OPTION) {
            updateRequestStatus(demandeId, "validé");
            JOptionPane.showMessageDialog(this, "Demande validée avec succès!");
        } else if (choice == JOptionPane.NO_OPTION) {
            updateRequestStatus(demandeId, "refusé");
            JOptionPane.showMessageDialog(this, "Demande refusée avec succès!");
        }
    }

    private void updateRequestStatus(int demandeId, String statut) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "UPDATE demande SET statut = ? WHERE iddemande = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, statut);
                preparedStatement.setInt(2, demandeId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean statutDejaTraite(String statut) {
        return statut != null && (statut.equals("validé") || statut.equals("refusé"));
    }
}
