import fr.cyu.schoolmanagementsystem.model.passwordmanager.HashPassword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class DatabaseExample {
    public static void main(String[] args) {
        // Configuration de la connexion
        String url = "jdbc:mysql://localhost:3306/school_db"; // Ajustez l'URL si nécessaire
        String user = "root";
        String password = ""; // Pas de mot de passe




        // Requête d'insertion
        String sql = "INSERT INTO admins (id, email, firstname, lastname,password,salt, is_verified) VALUES (?, ?, ?, ?,?,?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            byte[] salt= HashPassword.generateSalt();
            String hashedPassword = HashPassword.hashPassword("admin",salt);

            String stringSalt = Base64.getEncoder().encodeToString(salt);

            UUID uuid = UUID.fromString("e2437152-2648-41ac-bdd6-5f6f273839b0");
            byte[] uuidAsBytes = convertUUIDToBytes(uuid);

            // Définition des valeurs pour chaque "?"
            statement.setBytes(1, uuidAsBytes);  // username
            statement.setString(2, "admin@admin.com");  // password
            statement.setString(3, "admin");  // email
            statement.setString(4, "admin");
            statement.setString(5, hashedPassword);// passWord
            statement.setString(6, stringSalt);// salt
            statement.setBoolean(7, true);


            // Exécution de l'insertion
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Admin inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static byte[] convertUUIDToBytes(UUID uuid) {
        byte[] uuidBytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) ((mostSigBits >> (8 * (7 - i))) & 0xFF);
            uuidBytes[8 + i] = (byte) ((leastSigBits >> (8 * (7 - i))) & 0xFF);
        }
        return uuidBytes;
    }
}
