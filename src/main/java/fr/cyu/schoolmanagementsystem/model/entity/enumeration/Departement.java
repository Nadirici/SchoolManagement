package fr.cyu.schoolmanagementsystem.model.entity.enumeration;

import lombok.Getter;

@Getter
public enum Departement {
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
