package javapass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public void initialisationDB() throws SQLException { //On etablis la connection avec la base de donnees
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        System.out.println("Connexion a la base de donnees etablie");
        ajoutTable_base();
    }

    public void ajoutTable_base() { //On cree les tables dans la base de donnees si elles n'existent pas
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

    public void ajout_utilisateur(String username, String  encrypted_textAndTag_verify, byte[] iv_verify, byte[] salt, String last_login) throws SQLException { //On ajout un utilisateur dans la base de donnees/ watchout aux type des parametres
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
        //System.out.println("utilisateur ajouté avec succès");
    }

    public static void ajout_mdp(int user_id, String website_name, String url, String encrypted_username, byte[] iv_username, byte[] iv_password) throws SQLException {
        //On ajout un mdp dans la base de donnees
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "INSERT INTO passwords VALUES (null,?,?,?,?,?)";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setInt(1, user_id);
        pstmt.setString(2, website_name);
        pstmt.setString(3, url);
        pstmt.setString(4, encrypted_username);
        pstmt.setBytes(5, iv_username);
        pstmt.setBytes(6, iv_password);
            
        pstmt.executeUpdate();
        pstmt.close();
        co.close();
        System.out.println("Mot de passe ajouté avec succès");
    }

    public ReturningUserValues get_user(String username) throws SQLException {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setString(1,username);
        ResultSet rs = pstmt.executeQuery(sql);

        //while(rs.next()) est utilisé dans certain cas, mais pas ici au cas ou je le laisse
        int user_id = rs.getInt("user_id");
        //on a deja username en parametre
        String  encrypted_textAndTag_verify = rs.getString("encrypted_textAndTag_verify");
        byte[] iv_verify = rs.getBytes("iv_verify");
        byte[] salt = rs.getBytes("salt");
        String last_login = rs.getString("last_login");

        ReturningUserValues rv = new ReturningUserValues(user_id, username, encrypted_textAndTag_verify, iv_verify, salt, last_login);
            
        pstmt.close();
        co.close();
        return rv;
    }

    public final class ReturningUserValues {
        private final int user_id;
        private final String username;
        private final String encrypted_textAndTag_verify;
        private final byte[] iv_verify;
        private final byte[] salt;
        private final String last_login;
        public ReturningUserValues(int user_id, String username, String encrypted_textAndTag_verify, byte[] iv_verify, byte[] salt, String last_login){
            this.user_id = user_id;
            this.username = username;
            this.encrypted_textAndTag_verify = encrypted_textAndTag_verify;
            this.iv_verify = iv_verify;
            this.salt = salt;
            this.last_login = last_login;
        }
        //peutetre ajouter getters
    }

    public ReturningMdpValues get_mdp(int user_id) throws SQLException {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "SELECT * FROM passwords WHERE user_id = ?";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setInt(1,user_id);
        ResultSet rs = pstmt.executeQuery(sql);

        //while(rs.next()) est utilisé dans certain cas, mais pas ici au cas ou je le laisse
        int password_id = rs.getInt("password_id");
        //on a deja user_id en parametre
        String website_name = rs.getString("website_name");
        String url = rs.getString("url");
        String encrypted_username = rs.getString("encrypted_username");
        byte[] iv_username = rs.getBytes("iv_username");
        byte[] iv_password = rs.getBytes("iv_password");

        ReturningMdpValues rv = new ReturningMdpValues(password_id, user_id, website_name, url, encrypted_username, iv_username, iv_password);
            
        pstmt.close();
        co.close();
        return rv;
    }

    public final class ReturningMdpValues {
        private final int password_id;
        private final int user_id;
        private final String website_name;
        private final String url;
        private final String encrypted_username;
        private final byte[] iv_username;
        private final byte[] iv_password;
        public ReturningMdpValues(int password_id, int user_id, String website_name, String url, String encrypted_username, byte[] iv_username, byte[] iv_password){
            this.password_id = password_id;
            this.user_id = user_id;
            this.website_name = website_name;
            this.url = url;
            this.encrypted_username = encrypted_username;
            this.iv_username = iv_username;
            this.iv_password = iv_password;
        }
        //peutetre ajouter getters
    }
}