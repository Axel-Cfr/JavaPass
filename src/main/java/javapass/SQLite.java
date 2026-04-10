package javapass;

/*══════════════════════════════════════════════════════════════════════════════════════════════════════════════
Importation des outils nécessaires
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*══════════════════════════════════════════════════════════════════════════════════════════════════════════════
Sources :

https://sqlite.org/docs.html
https://www.youtube.com/watch?v=TN_xTjbrzzc
utilisation de Java Database Connectivity (JDBC) API pour se connecter à une base de données SQLite.
Avec un système de pilotes, le jdbc.jar est notre pilote (au sein du pom.xml). 

*//*════════════════════════════════════════════════════════════════════════════════════════════════════════════
Explication de la Class :

Class SQLite utilisé de façon à gérer toutes les données utilisateurs dans l'application
Des chiffrements AES et ARGON2 sont implémentés afin que toutes les données soient sécurisées

══════════════════════════════════════════════════════════════════════════════════════════════════════════════*/


public class SQLite 
{
    /* 
    On établis la connection avec la base de données
    et lui ajoute les tables users et passwords
    si elles n'existent pas. 
    */
    public void initialisationDB() throws SQLException 
    { 
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        System.out.println("Connexion a la base de donnees etablie");
        ajoutTable_base();
    }

    /* 
    Cette méthode permet d'ajouter les tables de base à la base de données
    Seulement si celles-ci n'existent pas encore
    Elle est utilisé dans l'initialisation de l'application 
    */
    public void ajoutTable_base() throws SQLException 
    {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        Statement stmt = co.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE NOT NULL," +
                        "encrypted_textAndTag_verify TEXT NOT NULL," +
                        "iv_verify BLOB NOT NULL," +
                        "salt BLOB NOT NULL," +
                        "argon2Type INTEGER," +
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

    /* 
    Cette méthode ajoute un utilisateur dans la table users
    pour ce faire il faut renseigner :
        - un nom d'utilisateur
        - un chiffrement du nom d'utilisateur (pour les comparaisons)   ═╗    
        - un vecteur d'initialisation                                    ║ 
        - un sel (gérer les chiffrements)                                ╠═> géré de façon automatique
        - le type d'argon2                                               ║ 
        - le last_login                                                 ═╝
    */
    public void ajout_utilisateur(String username, String  encrypted_textAndTag_verify, 
                                byte[] iv_verify, byte[] salt, int argon2Type, 
                                String last_login) throws SQLException 
    {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "INSERT INTO users VALUES (null,?,?,?,?,?,?)";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, encrypted_textAndTag_verify);
        pstmt.setBytes(3, iv_verify);
        pstmt.setBytes(4, salt);
        pstmt.setInt(5, argon2Type);
        pstmt.setString(6, last_login);
        
        pstmt.executeUpdate();
        pstmt.close();
        co.close();
        //System.out.println("utilisateur ajouté avec succès");
    }




    /* 
    Cette méthode ajoute un mot de passe dans la table passwords
    pour ce faire il faut renseigner :
        - un id utilisateur en temps que cle étrangère
        - un nom pour le website concerné
        - un url pour lier au website
        - un nom d'utilisateur encrypté                                 ═╗
        - un mot de passe encrypté                                       ║
        - un vecteur d'initialisation du nom                             ╠═> géré de façon automatique
        - un vecteur d'initialisation du password                       ═╝ 
    */
    public void ajout_mdp(int user_id, String website_name, 
                                String url, String encrypted_username, 
                                String encrypted_password, byte[] iv_username, 
                                byte[] iv_password) throws SQLException 
        {
        
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "INSERT INTO passwords VALUES (null,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setInt(1, user_id);
        pstmt.setString(2, website_name);
        pstmt.setString(3, url);
        pstmt.setString(4, encrypted_username);
        pstmt.setString(5, encrypted_password);
        pstmt.setBytes(6, iv_username);
        pstmt.setBytes(7, iv_password);
            
        pstmt.executeUpdate();
        pstmt.close();
        co.close();
        System.out.println("Mot de passe ajouté avec succès");
    }

    /*
    Cette méthode permet simplement à l'aide d'une requete SQLite de récuperer
    toutes les valeurs stockées en BDD d'un utilisateur
    */
    public UserValues get_user(String usernameTyped) throws SQLException
    {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setString(1,usernameTyped);
        ResultSet rs = pstmt.executeQuery();

        //while(rs.next()) est utilisé dans certain cas, mais pas ici au cas ou je le laisse
        int user_id = rs.getInt("user_id");
        String username = rs.getString("username");
        String  encrypted_textAndTag_verify = rs.getString("encrypted_textAndTag_verify");
        byte[] iv_verify = rs.getBytes("iv_verify");
        byte[] salt = rs.getBytes("salt");
        int argon2Type = rs.getInt("argon2Type");
        String last_login = rs.getString("last_login");

        UserValues rv = new UserValues(user_id, username, encrypted_textAndTag_verify, iv_verify, salt, argon2Type, last_login);
            
        pstmt.close();
        co.close();
        return rv;
    }

    /*
    commenter la méthode utilisé
    */
    public final class UserValues 
    {
        private final int user_id;
        private final String username;
        private final String encrypted_textAndTag_verify;
        private final byte[] iv_verify;
        private final byte[] salt;
        private final int argon2Type;
        private final String last_login;

        public UserValues(int user_id, String username, 
                        String encrypted_textAndTag_verify, 
                        byte[] iv_verify, 
                        byte[] salt, int argon2Type, 
                        String last_login)
        {
            this.user_id = user_id;
            this.username = username;
            this.encrypted_textAndTag_verify = encrypted_textAndTag_verify;
            this.iv_verify = iv_verify;
            this.salt = salt;
            this.argon2Type = argon2Type;
            this.last_login = last_login;
        }

        public int getUser_id() 
        {
            return user_id;
        }

        public String getUsername() 
        {
            return username;
        }

        public String getEncrypted_textAndTag_verify() 
        {
            return encrypted_textAndTag_verify;
        }

        public byte[] getIv_verify() 
        {
            return iv_verify;
        }

        public byte[] getSalt() 
        {
            return salt;
        }

        public int getArgon2Type() 
        {
            return argon2Type;
        }

        public String getLast_login() 
        {
            return last_login;
        }
        
        }

    /*
    commenter la méthode utilisé
    */
    public ArrayList<MdpValues> get_mdp(int user_id) throws SQLException 
    {
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "SELECT * FROM passwords WHERE user_id = ?";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setInt(1,user_id);
        ResultSet rs = pstmt.executeQuery();

        ArrayList<MdpValues> rv = new ArrayList<>();
        
        while(rs.next()){
            int password_id = rs.getInt("password_id");
            //on a deja user_id en parametre
            String website_name = rs.getString("website_name");
            String url = rs.getString("url");
            String encrypted_username = rs.getString("encrypted_username");
            String encrypted_password = rs.getString("encrypted_password");
            byte[] iv_username = rs.getBytes("iv_username");
            byte[] iv_password = rs.getBytes("iv_password");
            
            rv.add(new MdpValues(password_id, user_id, website_name, url, encrypted_username, encrypted_password, iv_username, iv_password));
        }     
        pstmt.close();
        co.close();
        return rv;
    }

    public final class MdpValues 
    {
        private final int password_id;
        private final int user_id;
        private final String website_name;
        private final String url;
        private final String encrypted_username;
        private final String encrypted_password;
        private final byte[] iv_username;
        private final byte[] iv_password;

        public MdpValues(int password_id, int user_id, 
                        String website_name, String url, 
                        String encrypted_username, String encrypted_password, 
                        byte[] iv_username, byte[] iv_password)
        {
            this.password_id = password_id;
            this.user_id = user_id;
            this.website_name = website_name;
            this.url = url;
            this.encrypted_username = encrypted_username;
            this.encrypted_password = encrypted_password;
            this.iv_username = iv_username;
            this.iv_password = iv_password;
        }

        public int getPassword_id() 
        {
            return this.password_id;
        }

        public int getUser_id() 
        {
            return this.user_id;
        }

        public String getWebsite_name() 
        {
            return this.website_name;
        }

        public String getUrl() 
        {
            return this.url;
        }

        public String getEncrypted_username() 
        {
            return this.encrypted_username;
        }

        public String getEncrypted_password() 
        {
            return this.encrypted_password;
        }

        public byte[] getIv_username() 
        {
            return this.iv_username;
        }

        public byte[] getIv_password() 
        {
            return this.iv_password;
        }
    }



    /*
    commenter la méthode utilisé
    */
    //On supprime un utilisateur via son username
    public void suppr_utilisateur(String username) throws SQLException 
    { 
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setString(1, username);
        
        pstmt.executeUpdate();
        pstmt.close();
        co.close();
    }


    //enlever static
    //On supprime un utilisateur via son username
    public void suppr_mdp(int user_id,String website_name) throws SQLException 
    { 
        Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
        ///String sql = "DELETE FROM passwords AS p JOIN users AS u ON u.user_id = p.user_id AND p.website_name = ?";
        String sql = "DELETE FROM passwords WHERE website_name = ? AND user_id = ?;";
        PreparedStatement pstmt = co.prepareStatement(sql);
        pstmt.setString(1, website_name);
        pstmt.setInt(2, user_id);
        pstmt.executeUpdate();
        pstmt.close();
        co.close();
        System.out.println("utilisateur supprimé avec succès");
    }
}
