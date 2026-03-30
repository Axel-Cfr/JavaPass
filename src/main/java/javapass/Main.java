package javapass;

public class Main {
    public static void main(String[] args) {
        try {
            Services services = new Services();
            Interface interface_utilisateur = new Interface(services);
            interface_utilisateur.afficherBienvenue();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    	//String mdp = Services.generate(150, true, true, true, true);
        //System.out.println("Mot de passe généré : " + mdp);
    }
}