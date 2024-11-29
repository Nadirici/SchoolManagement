package fr.cyu.schoolmanagementsystem.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class InputValidator {

    // Expression régulière pour valider le format d'un email
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Expression régulière pour valider les noms et prénoms
    private static final String NAME_REGEX = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    /**
     * Vérifie si une adresse email est valide.
     *
     * @param email l'adresse email à vérifier
     * @return true si l'email est valide, false sinon
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Vérifie si une date de naissance est cohérente.
     * La date doit être au format "yyyy-MM-dd", ne pas être dans le futur et être après 1900.
     *
     * @param birthDate la date de naissance sous forme de chaîne (format "yyyy-MM-dd")
     * @return true si la date est valide et cohérente, false sinon
     */
    public static boolean isValidBirthDate(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            return false;
        }

        try {
            // Convertir la chaîne en LocalDate
            LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);

            // Vérifier que la date n'est pas dans le futur et après 1900
            LocalDate today = LocalDate.now();
            LocalDate minDate = LocalDate.of(1900, 1, 1);

            return !date.isAfter(today) && !date.isBefore(minDate);

        } catch (DateTimeParseException e) {
            // Si la date est au mauvais format
            return false;
        }
    }

    /**
     * Vérifie si un nom ou prénom est valide.
     * Le nom ou prénom ne doit contenir que des lettres, des espaces et des tirets.
     *
     * @param name le nom ou prénom à vérifier
     * @return true si le nom ou prénom est valide, false sinon
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches();
    }

    public static void main(String[] args) {
        // Tests pour les emails
        System.out.println(isValidEmail("test@example.com")); // true
        System.out.println(isValidEmail("invalid-email"));    // false
        System.out.println(isValidEmail(""));                // false
        System.out.println(isValidEmail(null));              // false

        // Tests pour les noms et prénoms
        System.out.println(isValidName("Jean Dupont"));       // true
        System.out.println(isValidName("Marie-Thérèse"));     // true
        System.out.println(isValidName("123Jean"));           // false
        System.out.println(isValidName("Jean@Dupont"));       // false
        System.out.println(isValidName(""));                 // false
        System.out.println(isValidName(null));               // false
    }
}
