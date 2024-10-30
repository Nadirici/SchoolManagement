package servlets.schoolmanagement.models.Enumerations;

import lombok.Getter;

@Getter
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

    public String getName() {
         return nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public static Departement fromDisplayName(String displayName) {
        for (Departement subject : Departement.values()) {
            if (subject.getName().equalsIgnoreCase(displayName)) {
                return subject;
            }
        }
        throw new IllegalArgumentException("Aucun sujet correspondant pour le nom : " + displayName);
    }
}
