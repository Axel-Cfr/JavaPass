package javapass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

//https://www.youtube.com/watch?v=TN_xTjbrzzc
//SQLite permet de stocker des données localement fournis avec l'application.
//utilise Java Database Connectivity (JDBC) API pour se connecter à une base de données SQLite.
//Avec un système de pilotes, le jdbc.jar est notre pilote.

public class SQLite {
    /*public static void main(String[] args) {
        initialisationDB();
        ajoutTable_base();
    }*/

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
                            "username TEXT UNIQUE NOT NULL," +
                            "encrypted_textAndTag_verify TEXT NOT NULL," +
                            "iv_verify BLOB NOT NULL," +
                            "salt BLOB NOT NULL," +
                            "last_login TEXT" +
                        ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                            "password_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id INTEGER NOT NULL," +
                            "website_name TEXT NOT NULL," +
                            "url TEXT," +
                            "encrypted_username TEXT NOT NULL," +
                            "encrypted_password TEXT NOT NULL," +
                            "iv_username BLOB NOT NULL," +
                            "iv_password BLOB NOT NULL," +

                            "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                        ");";
                        /*"-- En option"
                        category TEXT,
                        notes TEXT,
                        created_at TEXT,
                        updated_at TEXT,
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
    public static void ajout_utilisateur(String username, String  encrypted_textAndTag_verify, byte[] iv_verify, byte[] salt, String last_login) { //On ajout un utilisateur dans la base de donnees/ watchout aux type des parametres
        try {
            Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
            String sql = "INSERT INTO users VALUES (null,?,?,?,?,?)";
            PreparedStatement pstmt = co.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, encrypted_textAndTag_verify);
            pstmt.setBytes(3, iv_verify);
            pstmt.setBytes(4, salt);
            pstmt.setString(5, last_login);
            
            pstmt.executeUpdate();
            pstmt.close();
            co.close();
            System.out.println("utilisateur ajouté avec succès");
        } 
        catch (SQLException e) 
        {
            System.err.println(e.getMessage());
        }
    }
}
/*  
on va créer des méthodes permetant de recup : 
encrypted_textAndTag_verify
iv_verify
salt
*/