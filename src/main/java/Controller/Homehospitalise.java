package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import Model.Utilisateur;

public class Homehospitalise extends JFrame {

    private static Utilisateur utilisateur;
    private static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_005";
    private static final String USER = "projet_gei_005";
    private static final String PASSWORD = "Sie2ooch";

    private JPanel profilPanel;
    private JPanel demandePanel;

    public Homehospitalise(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this, "Identifiants invalides.");
            return;
        }
    
        setTitle("Accueil");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        setLayout(new BorderLayout());
    
        profilPanel = new JPanel(new GridLayout(4, 2));
        profilPanel.setBorder(BorderFactory.createTitledBorder("Informations du Profil"));
        profilPanel.setBackground(new Color(100, 100, 0));
    
        loadUserInfoFromDatabase();
    
        add(profilPanel, BorderLayout.NORTH);
    
        demandePanel = new JPanel(new GridLayout(0, 1));
        demandePanel.setBorder(BorderFactory.createTitledBorder("Mes Demandes"));
        demandePanel.setBackground(Color.white);
    
        updateDemandePanel();
    
        JButton ajouterDemandeButton = new JButton("Ajouter une Demande");
        ajouterDemandeButton.setBackground(new Color(255, 165, 0));
        ajouterDemandeButton.setForeground(Color.white);
        ajouterDemandeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String typeAide = JOptionPane.showInputDialog("Type de l'aide:");
                String description = JOptionPane.showInputDialog("Description de la demande:");
    
                ajouterDemande(utilisateur.getNom(), typeAide, description);
                updateDemandePanel();
                JOptionPane.showMessageDialog(Homehospitalise.this, "Demande ajoutée avec succès!");
            }
        });
    
        add(ajouterDemandeButton, BorderLayout.SOUTH);
    
        setVisible(true);
    }
    
    private void loadUserInfoFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT username, email, role FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, utilisateur.getNom());
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("role");
    
                    profilPanel.add(new JLabel("Nom: " + username));
                    profilPanel.add(new JLabel("Email: " + email));
                    profilPanel.add(new JLabel("Rôle: " + role));
    
                    profilPanel.revalidate();
                    profilPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Utilisateur non trouvé");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données: " + e.getMessage());
        }
    }
    
    private void ajouterDemande(String demandeur, String typeAide, String description) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "INSERT INTO demande (typeaide, description, demandeur, statut) VALUES (?, ?, ?, 'En attente')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, typeAide);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, demandeur);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDemandePanel() {
        demandePanel.removeAll();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT typeaide, description, statut FROM demande WHERE demandeur = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, utilisateur.getNom());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String typeAide = resultSet.getString("typeaide");
                    String description = resultSet.getString("description");
                    String statut = resultSet.getString("statut");

                    JPanel demandeInfoPanel = new JPanel(new GridLayout(1, 3));
                    demandeInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                    demandeInfoPanel.setBackground(Color.white);

                    JLabel typeAideLabel = new JLabel("Type d'aide: " + typeAide);
                    demandeInfoPanel.add(typeAideLabel);

                    JLabel statutLabel = new JLabel("Statut: " + (statut != null ? statut : "En attente"));
                    demandeInfoPanel.add(statutLabel);

                    demandePanel.add(demandeInfoPanel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        validate();
        repaint();
    }
}
