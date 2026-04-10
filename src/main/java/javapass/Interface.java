package javapass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interface {
    public final String GREEN = "\033[0;32m"; //Green
    public final String RED = "\033[0;31m";
    public final String YELLOW = "\033[0;33m";
    Services services;

    public Interface(Services services) {
        this.services =  services;
    }

    public void afficherBienvenue() {
        clearScreen();
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
        "                    '{                   Créé par Axel Chabot, Morgann Morvan et Octave Girault",
        "             .'';I!<_]{1((){]~l,'                             ",
        "           >\\+I`              ^>))                           ",
        "           fj/}+l,`'.      ''I<](rI      Tapez [L] pour vous connecter",
        "           \\:   .^,:;IllII;:''`   )^                          ",
        "    .]/f/1^)I_~                  \\       Tapez [S] pour vous inscrire ",
        "   It'.~+.iri<]    ;-1)){+''      /.                          ",
        "   (><\\' [\\t<l1  [         '+    /'                          ",
        "  .f'\\l   ~r>;(  ]   [)|i  `+    /'      Tapez une autre touche pour quitter",
        "  ./'|>    /!:\\  ]  +t||t, `+    /                           ",
        "   ]1,/;   tII|  ]  [rrrrl ,<    t                           ",
        "    {}.<\\/jjIi(  ,|'.```` :(.   .t       Puis appuyez sur [Entrée]        ",
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
        try {
            Scanner scanner = new Scanner(System.in);
            String action = scanner.nextLine();

            if(action.equals("L") || action.equals("l")) {
		    	bandeau();
                connection();
		    } else if(action.equals("S") || action.equals("s")) {
                bandeau();
		    	inscription();
		    } else {
                scanner.close();
		    	System.exit(0);
		    }
        } catch(Exception e) {
            erreur(e.getMessage());
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
        System.err.println(RED + "Une erreur s'est produite, veuillez redémarrer JavaPass");
        System.err.println("Détail de l'erreur :\n"+e + GREEN);
        System.exit(0);
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
            accueil();
        } else if(result.equals("Wrong")) {
            System.out.println("\n Veuillez entrer des identifiants valides");
            services.wait(3000);
            connection();
        } else {
            scanner.close();
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
            services.wait(2000);
            afficherBienvenue();
        } else if(result.equals("Different")) {
            System.out.println(RED + "\nVeuillez entrer le même mot de passe" + GREEN);
            services.wait(3000);
            inscription();
        } else {
            scanner.close();
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
        System.out.println("[3] : Modifier mon mot de passe maître");
        System.out.println("[4] : Supprimer mon compte");
        System.out.println("[5] : Quitter JavaPass");
        System.out.println("\nEntrez votre choix (1, 2, 3, 4 ou 5): ");
        String option = scanner.nextLine();

        if(option.equals("1")) {
            voirListeMDP();
        } else if(option.equals("2")) {
            ajouterMDP();
        } else if(option.equals("3")) {
            System.out.println("\nLa mise à jour du mot de passe maître n'est pas encore implémentée.");
            services.wait(2000);
            accueil();
        } else if(option.equals("4")) {
            boolean choice = true;
            while(choice) {
                System.out.println("Toutes vos données et vos mots de passe seront définitivement perdus");
                System.out.println("Êtes vous sûr(e) de vouloir supprimer votre compte ? [O/N]");
                String confirm = scanner.nextLine();
                if(confirm.equals("O") || confirm.equals("o")) {
                    choice = false;
                    String resultat = services.deleteAccount();
                    if(!resultat.equals("Done")) {
                        scanner.close();
                        erreur(resultat);
                    }
                    System.out.println("Compte suprimé avec succès");
                    services.wait(2000);
                    afficherBienvenue();
                } else if(confirm.equals("N") || confirm.equals("n")) {
                    choice = false;
                    accueil();
                } else {
                    System.out.println("Veuillez faire une entrée valide");
                }
            }
        } else if(option.equals("5")) {
            System.out.println("Passez une bonne journée :D");
            scanner.close();
            services.wait(3000);
            System.exit(0);
        } else {
            accueil();
        }
    }

    public void voirListeMDP() {
        ArrayList<String> websiteNameList = services.returnWebsiteName();
        String resultatRecherche = "In progress";
        Scanner scanner = new Scanner(System.in);

        while(!resultatRecherche.equals("Done")) {
            clearScreen();
            bandeau();
        
            System.out.println("Quel mot de passe voulez-vous afficher ?");
            System.out.println("Entrez le numéro du site");
            System.out.println("Ou entrez [s*] puis votre saisie pour rechercher un mot de passe");
            System.out.println("Entrez [Q] pour quitter\n");

            for(int i = 0; i < websiteNameList.size(); i++) {
                System.out.println("["+(i+1)+"] "+websiteNameList.get(i));
            }

            String option = scanner.nextLine();
            if(option.startsWith("s*")) {
                websiteNameList = services.researchWebsiteName(option.substring(2, option.length()));
            } else if (option.equals("Q") || option.equals("q")) {
                accueil();
            } else {
                try {   
                    int intOption = Integer.parseInt(option);
                    if(intOption >= 1 && intOption <= websiteNameList.size()) {
                        String[] passwordInfos = services.givePasswordInfos(websiteNameList.get(intOption-1));
                        if(passwordInfos.length == 1) {
                            scanner.close();
                            erreur(passwordInfos[0]);
                        } else {
                            resultatRecherche = "Done";
                            voirMDP(passwordInfos);
                        }
                    } else {
                        System.out.println(RED + "\nVeuillez faire une entrée valide" + GREEN);
                        services.wait(3000);
                    }
                
                } catch (Exception e) {
                    System.out.println(RED + "\nVeuillez faire une entrée valide" + GREEN);
                    services.wait(3000);
                }
            }
        }
    }

    // Fonction qui affiche les informations du mot de passe choisi
    // (Nom du site, url, nom d'utilisateur ou email, mot de passe)
    public void voirMDP(String[] passwordInfos) {
        Scanner scanner = new Scanner(System.in);

        boolean done = false;
        while(!done) {
            clearScreen();
            bandeau();

            String websiteName = passwordInfos[0];
            String url = passwordInfos[1];
            String username = passwordInfos[2];
            String password = passwordInfos[3];

            System.out.println("Nom du site : "+websiteName);
            if(!(url == null || url.isBlank())) {
                System.out.println("\nUrl du site : "+url);
            }
            System.out.println("\nNom d'utilisateur : "+username);
            System.out.println("\nMot de passe : "+password);

            System.out.println("\n\nActions disponibles :");
            System.out.println("[1] Retour a la liste");
            System.out.println("[2] Supprimer ce mot de passe");
            System.out.println("[3] Modifier ce mot de passe");
            System.out.println("[4] Analyser ce mot de passe");
            System.out.println("\nEntrez votre choix (1, 2, 3 ou 4): ");

            String option = scanner.nextLine();
            if(option.equals("1")) {
                done = true;
                voirListeMDP();
            } else if(option.equals("2")) {
                while(true) {
                    System.out.println("\nConfirmer la suppression ? [O/N]");
                    String confirm = scanner.nextLine();

                    if(confirm.equals("O") || confirm.equals("o")) {
                        String result = services.deletePassword(websiteName);
                        if(result.equals("Done")) {
                            System.out.println("\nMot de passe supprimé avec succés.");
                            services.wait(2000);
                            done = true;
                            voirListeMDP();
                        } else {
                            erreur(result);
                        }
                        break;
                    } else if(confirm.equals("N") || confirm.equals("n")) {
                        break;
                    } else {
                        System.out.println(RED + "Veuillez répondre par O ou N." + GREEN);
                        services.wait(1200);
                    }
                }
            } else if(option.equals("3")) {
                System.out.println("\nLa mise à jour du mot de passe n'est pas encore implémentée.");
                services.wait(2000);
            } else if(option.equals("4")) {
                clearScreen();
                bandeau();
                System.out.println("Nom du site : "+websiteName);
                if(!(url == null || url.isBlank())) {
                    System.out.println("\nUrl du site : "+url);
                }
                System.out.println("\nNom d'utilisateur : "+username);
                System.out.println("\nMot de passe : "+password + "\n");

                System.out.println(services.analysePassword(password));
                
                ArrayList<String> reusedWebsites = services.samePassword(password, websiteName);
                if (reusedWebsites.size() > 0) {
                    System.out.print(YELLOW + "\nATTENTION : Ce mot de passe est réutilisé sur le(s) site(s) suivant(s) : ");
                    for (int i = 0; i < reusedWebsites.size(); i++) {
                        System.out.print(reusedWebsites.get(i));
                        if (i < reusedWebsites.size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println(". Nous vous recommandons de les modifier." + GREEN);
                } else {
                    System.out.println("\nCe mot de passe n'est pas réutilisé.");
                }

                if (services.estFaible(password)) {
                    System.out.println(YELLOW + "\nCe mot de passe est considéré comme faible." + GREEN);
                    System.out.println("\nVoulez-vous le modifier ?");
                    System.out.println("[1] L'améliorer");
                    System.out.println("[2] En générer un nouveau");
                    System.out.println(RED + "[3] Ne rien faire" + GREEN);
                    System.out.print("\nEntrez votre choix (1, 2 ou 3): ");
                    
                    String choice = scanner.nextLine();
                    String newPassword = null;
                    if (choice.equals("1")) {
                        newPassword = services.enhancePassword(password);
                        System.out.println("\nNouveau mot de passe amélioré : " + newPassword);
                    } else if (choice.equals("2")) {
                        newPassword = services.generatePassword(20, true, true, true, true);
                        System.out.println("\nNouveau mot de passe généré : " + newPassword);
                    }
                    
                    if (newPassword != null) {
                        System.out.println("\nVoulez-vous remplacer l'ancien mot de passe par celui-ci ? [O/N]");
                        String confirmUpdate = scanner.nextLine();
                        if (confirmUpdate.equals("O") || confirmUpdate.equals("o")) {
                            System.out.println("\nLa mise à jour du mot de passe n'est pas encore implémentée.");
                        } else {
                            System.out.println("\nMise à jour annulée. L'ancien mot de passe est conservé.");
                        }
                    }
                }
                
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
            else {
                System.out.println(RED + "\nVeuillez faire une entrée valide" + GREEN);
                services.wait(1500);
            }
        }
        scanner.close();
    }

    // Fonction permettant d'ajouter un mot de passe et ses informations complémentaires
    public void ajouterMDP() {
        boolean sortie = false;
        
        while(!sortie) {
            clearScreen();
            bandeau();
            System.out.println("Ajout d'un nouveau mot de passe\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEntrez le nom du site");
            String websiteName = scanner.nextLine();
            System.out.println("\nEntrez l'url du site (optionnel)");
            String url = scanner.nextLine();
            System.out.println("\nEntrez le nom d'utilisateur ou l'email pour ce site");
            String username = scanner.nextLine();
            System.out.println("\nQue voulez vous faire ?");
            System.out.println("[1] : Saisir votre mot de passe");
            System.out.println("[2] : Générer un mot de passe");
            System.out.println("\nEntrez votre choix (1 ou 2): ");
            String choice = scanner.nextLine();

            String password = "";
            if(choice.equals("1")) {
                System.out.println("\nEntrez le mot de passe pour ce site");
                password = scanner.nextLine();
            } else if(choice.equals("2")) {
                password = services.generatePassword(20, true, true, true, true);
            }

            if(!(websiteName.isBlank() || username.isBlank() || password.isBlank())) {
                String[] passwordInfo = {websiteName, url, username, password};
                String retour = services.addNewPassword(passwordInfo);
                if(retour.equals("Done")) {
                    accueil();
                } else {
                    scanner.close();
                    erreur(retour);
                }
            } else {
                System.out.println(RED + "\nMerci de faire des entrées valides" + GREEN);
                services.wait(3000);
            }
        }
    }
}
