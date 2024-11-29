package fr.cyu.schoolmanagementsystem.entity.enumeration;

public enum Department {
    SCIENCES("SCIENCES"),
    LITTERATURE("LITTERATURE"),
    MATHEMATIQUES("MATHEMATIQUES"),
    HISTOIRE("HISTOIRE"),
    GEOGRAPHIE("GEOGRAPHIE"),
    INFORMATIQUE("INFORMATIQUE"),
    ARTS("ARTS"),
    PHYSIQUE("PHYSIQUE"),
    CHIMIE("CHIMIE"),
    LANGUES("LANGUES");

    private final String nom;

    Department(String nom) {
        this.nom = nom;
    }

    public String getName() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    public static Department fromDisplayName(String displayName) {
        for (Department subject : Department.values()) {
            if (subject.getName().equalsIgnoreCase(displayName)) {
                return subject;
            }
        }
        throw new IllegalArgumentException("Aucun sujet correspondant pour le nom : " + displayName);
    }
}
