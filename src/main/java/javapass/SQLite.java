package javapass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//https://www.youtube.com/watch?v=TN_xTjbrzzc
//SQLite permet de stocker des données localement fournis avec l'application.
//utilise Java Database Connectivity (JDBC) API pour se connecter à une base de données SQLite.
//Avec un système de pilotes, le jdbc.jar est notre pilote.

public class SQLite {
    
    public static void initialisationDB() { //On etablis la connection avec la base de donnees
        try {
            Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
            System.out.println("Connexion a la base de donnees etablie");
            ajoutTable_base();
        }
        catch(SQLException e)// On gere les exceptions liees a la base de donnees (tres important))
        {
            System.err.println(e.getMessage());// message d'erreur
        }
    }

    public static void ajoutTable_base() { //On cree les tables dans la base de donnees si elles n'existent pas
        try {
            Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement stmt = co.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                            "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "username VARCHAR(100) UNIQUE NOT NULL," +
                            "encrypted_textAndTag_verify VARBINARY(128) NOT NULL," +
                            "iv_verify BINARY(12) NOT NULL," +
                            "salt BINARY(16) NOT NULL," +
                            "last_login TIMESTAMP" +
                        ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                            "password_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id INTEGER NOT NULL," +
                            "website_name VARCHAR(100) NOT NULL," +
                            "url VARCHAR(100)," +
                            "encrypted_username VARBINARY(128) NOT NULL," +
                            "encrypted_password VARBINARY(128) NOT NULL," +
                            "iv_username binary(12) NOT NULL," +
                            "iv_password binary(12) NOT NULL," +

                            "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                        ");";
                        /*"-- En option"
                        category VARCHAR(50),
                        notes TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        */
            stmt.executeUpdate(sql);
            stmt.close();
            co.close();
            System.out.println("Tables créées avec succès");
        }
        catch(SQLException e)// On gere les exceptions liees a la base de donnees (tres important))
        {
            System.err.println(e.getMessage());// message d'erreur
        }
    }
}
