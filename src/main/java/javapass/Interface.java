package javapass;

import java.io.IOException;
import java.util.Scanner;

public class Interface {
    public final String GREEN = "\033[0;32m"; //Green
    Services services = new Services();

    public void afficherBienvenue() throws Exception {
        String result = services.connectionDB();
        if(!result.equals("Done")) {
            erreur(result);
        }
        String[] logo = {
        "                                                             ",
        "                                                             ",
        "                                                             ",
        "                                                             ",
        "                          i                                  ",
        "                         +jI                                 ",
        "                          (rf{'                              ",
        "                        ^l '|rrt.                            ",
        "                      .;tl   (rr^        Bienvenue sur       ",
        "                .:]fj/]^  `l//-                              ",
        "              ,tr)^    If1:                   ██╗ █████╗ ██╗   ██╗ █████╗ ██████╗  █████╗ ███████╗███████╗",
        "             `/rj      \\:                     ██║██╔══██╗██║   ██║██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔════╝",
        "             .(rr}.                           ██║███████║██║   ██║███████║██████╔╝███████║███████╗███████╗",
        "               :1rrj(_'                  ██   ██║██╔══██║╚██╗ ██╔╝██╔══██║██╔═══╝ ██╔══██║╚════██║╚════██║",
        "                   .`:_trt?              ╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║██║     ██║  ██║███████║███████║",
        "                         Irf'             ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚══════╝╚══════╝",
        "                     '{tfrj)                                 ",
        "                    '{                   Created by Axel Chabot, Morgann Morvan and Octave Girault",
        "             .'';I!<_]{1((){]~l,'                             ",
        "           >\\+I`              ^>))                           ",
        "           fj/}+l,`'.      ''I<](rI      Press [L] to log in  ",
        "           \\:   .^,:;IllII;:''`   )^                          ",
        "    .]/f/1^)I_~                  \\       Press [S] to sign up ",
        "   It'.~+.iri<]    ;-1)){+''      /.                          ",
        "   (><\\' [\\t<l1  [         '+    /'                          ",
        "  .f'\\l   ~r>;(  ]   [)|i  `+    /'      Press any other key to quit",
        "  ./'|>    /!:\\  ]  +t||t, `+    /                           ",
        "   ]1,/;   tII|  ]  [rrrrl ,<    t                           ",
        "    {}.<\\/jjIi(  ,|'.```` :(.   .t       Then press [enter]   ",
        "     ;\\(]1jj;<}    !1]<~}},     '\\                           ",
        "          `f,+_                 :(                           ",
        "           /<                  '/]                           ",
        "            ^|j]:'.      ..'']jf>                             ",
        "                  'Ii<+~i;.                                  ",
        "                                                             ",
        };
        for (String lines : logo) {
            System.out.println(GREEN + lines);
        }
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        if(action.equals("L") || action.equals("l")) {
			bandeau();
            connection();
		} else if(action.equals("S") || action.equals("s")) {
            bandeau();
			inscription();
		} else {
			System.exit(0);
		}
    }

    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch(IOException | InterruptedException e) {
            erreur(e.getMessage());
        }
    }

    public void bandeau() {
        String[] lines = {
            "===========================================================================================================",
            "===========================================================================================================",
            "====================      ██╗ █████╗ ██╗   ██╗ █████╗ ██████╗  █████╗ ███████╗███████╗ ====================",
            "====================      ██║██╔══██╗██║   ██║██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔════╝ ====================",
            "====================      ██║███████║██║   ██║███████║██████╔╝███████║███████╗███████╗ ====================",
            "==================== ██   ██║██╔══██║╚██╗ ██╔╝██╔══██║██╔═══╝ ██╔══██║╚════██║╚════██║ ====================",
            "==================== ╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║██║     ██║  ██║███████║███████║ ====================",
            "====================  ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚══════╝╚══════╝ ====================",
            "===========================================================================================================",
            "==========================================================================================================="
        };
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println("\n\n");
    }

    public void erreur(String e) {
        clearScreen();
        bandeau();
        System.err.println("Une erreur s'est produite, veuillez redémarrer JavaPass");
        System.err.println("Détail de l'erreur :\n"+e);
    }

    public void connection() {
        clearScreen();
        bandeau();

        System.out.print("Identifiant : ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.print("\n\nMot de passe : ");
        String password = scanner.nextLine();
        
        String result = services.authentification(username, password);
        if(result.equals("Done")) {
            System.out.println("Connexion...");
        } else if(result.equals("Wrong")) {
            System.out.println("\n Mot de passe erroné");
            services.wait(3000);
            connection();
        } else {
            erreur(result);
        }
    }

    public void inscription() {
        clearScreen();
        bandeau();

        System.out.println("Identifiant: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("\nMot de passe : ");
        String password = scanner.nextLine();
        System.out.println("\nConfirmer votre mot de passe : ");
        String passwordverif = scanner.nextLine();
        System.out.println("\nEntrez le type de machine sur lequel vous utilisez JavaPass");
        System.out.println("[1] : Ordinateur puissant strictement personnel");
        System.out.println("[2] : Serveur");
        System.out.println("[3] : Autres");
        System.out.println("\nEntrez votre choix (1, 2 ou 3): ");
        String option = scanner.nextLine();

        String result = services.inscription(username, password, passwordverif, option);
        if(result.equals("Done")) {
            System.out.println("Utilisateur crée avec succès");
        } else if(result.equals("Different")) {
            System.out.println("\nEntrez le même mot de passe");
            services.wait(3000);
            inscription();
        } else {
            erreur(result);
        }
    }

    public void accueil() {
        clearScreen();
        bandeau();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bonjour "+services.user.getUsername());
        System.out.println("Dernière connexion "+services.user.getLast_login());
        System.out.println("\nQue voulez vous faire ?");
        System.out.println("[1] : Consulter mes mots de passe");
        System.out.println("[2] : Ajouter un nouveau mot de passe");
        System.out.println("[3] : Quitter JavaPass");
        System.out.println("\nEntrez votre choix (1, 2 ou 3): ");
        String option = scanner.nextLine();

        if(option.equals("1")) {
            System.out.println("Consultation des mots de passe...");
        } else if(option.equals("2")) {
            System.out.println("Ajout d'un nouveau mot de passe...");
        } else if(option.equals("3")) {
            System.out.println("Passez une bonne journée :D");
            services.wait(3000);
            System.exit(0);
        } else {
            accueil();
        }
    }
}
