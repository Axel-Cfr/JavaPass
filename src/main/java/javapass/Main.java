package javapass;

public class Main {
    public static void main(String[] args) {
        try {
            Services services = new Services();
            Interface interface_utilisateur = new Interface(services);

            InactivityCounter compteurInactivite = new InactivityCounter();
            compteurInactivite.start();

            
            interface_utilisateur.afficherBienvenue();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
} 