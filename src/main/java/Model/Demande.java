package Model;

public class Demande {
    private String typeAide;
    private String description;
    private String statut;
    private String demandeur;

    public Demande(String typeAide, String description, String statut, String demandeur) {
        this.typeAide = typeAide;
        this.description = description;
        this.statut = statut;
        this.demandeur = demandeur;
    }

    public String getTypeAide() {
        return typeAide;
    }

    public String getDescription() {
        return description;
    }

    public String getStatut() {
        return statut;
    }

    public String getDemandeur() {
        return demandeur;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "typeAide='" + typeAide + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", demandeur='" + demandeur + '\'' +
                '}';
    }
}
