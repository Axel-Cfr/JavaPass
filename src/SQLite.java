import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//https://www.youtube.com/watch?v=TN_xTjbrzzc
//SQLite permet de stocker des données localement fournis avec l'application.
//utilise Java Database Connectivity (JDBC) API pour se connecter à une base de données SQLite.
//Avec un système de pilotes, le jdbc.jar est notre pilote.
public class SQLite {

/////////////////////////////////////////////////////////////////////////////////////////////

    public static void initialisationDB() //On etablis la connection avec la base de donnees
    {
        try{
            Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
            //Statement stmt = co.createStatement();
            System.out.println("Connexion a la base de donnees etablie");
        }
        catch(SQLException e)// On gere les exceptions liees a la base de donnees (tres important))
        {
            System.err.println(e.getMessage());// message d'erreur
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////

    public static void ajoutTable_base()//On cree une table dans la base de donnees
    {
        try{
            Connection co = DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement stmt = co.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user " +
                         "(user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         " username      TEXT    NOT NULL, " +
                         " , " +
                         " )";
            stmt.executeUpdate(sql);
            stmt.close();
            co.close();
            System.out.println("Table creee avec succes");
        }
        catch(SQLException e)// On gere les exceptions liees a la base de donnees (tres important))
        {
            System.err.println(e.getMessage());// message d'erreur
        }
    }
}
