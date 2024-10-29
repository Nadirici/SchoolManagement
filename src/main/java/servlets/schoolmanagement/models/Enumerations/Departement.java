package servlets.schoolmanagement.models.Enumerations;

public enum Departement {
    SCIENCES("Sciences"),
    LITTERATURE("Littérature"),
    MATHEMATIQUES("Mathématiques"),
    HISTOIRE("Histoire"),
    GEOGRAPHIE("Géographie"),
    INFORMATIQUE("Informatique"),
    ARTS("Arts"),
    PHYSIQUE("Physique"),
    CHIMIE("Chimie"),
    LANGUES("Langues");

    private final String nom;

    Departement(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
