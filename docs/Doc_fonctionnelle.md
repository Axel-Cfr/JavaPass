# Documentation Fonctionnelle de JavaPass

## Prérequis

- [JDK Java 25](https://adoptium.net/fr/temurin/releases)
- [Git 2.52.0](https://git-scm.com/install)
- [Maven 3.9.12](https://maven.apache.org/download.cgi)

Si vous n'avez pas configuré Maven, cliquez ici pour voir le [guide de configuration Maven](Tuto_Maven.md)

## Installation et Mise en place

1. Ouvrez l'invite de commandes et rendez-vous dans le répertoire où vous voulez que le dépôt soit cloné
```
cd chemin/où_je_veux/installer/JavaPass
```

2. Téléchargez le dépôt
```
git clone https://github.com/Axel-Cfr/JavaPass.git
```

3. Rendez-vous dans le répertoire racine de JavaPass
```
cd JavaPass
```

4. Nettoyez les potentiels fichiers générés par Maven, compilez et installez les fichiers du projet 
```
mvn clean install
```

5. Indiquez à la console d'afficher les caractères avec l'encodage UTF-8 (sinon certains caractères spéciaux s'afficheront mal).
- **Vous pouvez passer cette étape si vous utilisez un système Linux/Unix**
```
C:\Windows\System32\chcp.com 65001
```

6. Lancez JavaPass
```
java -jar target/JavaPass.jar
```

7. Optionnel pour **Windows**: Vous pouvez créer un fichier JavaPass.bat cliquable en y écrivant ceci dans un fichier texte et en l'enregistrant sous forme de fichier .bat :
```
C:\Windows\System32\chcp.com 65001
cd C:/Users/votre_user/chemin/vers/JavaPass
java -jar target/JavaPass.jar
```

7. Optionnel pour **Linux/Unix**: Vous pouvez créer un fichier JavaPass.sh cliquable en y écrivant ceci dans un fichier texte et en l'enregistrant sous forme de fichier .sh :
```
cd C:/home/votre_user/chemin/vers/JavaPass
java -jar target/JavaPass.jar
```

8. Optionnel : Vous pouvez créer un raccourci de JavaPass.bat et lui attribuer l'icône de JavaPass. Le fichier Javapass.ico se trouve dans `C:/Users/votre_user/chemin/vers/JavaPass/docs/img`.

**Attention**, si vous lancez JavaPass depuis un IDE sans passer par le .jar, veillez à exécuter `C:\Windows\System32\chcp.com 65001` avant. Autrement, certains caractères spéciaux s'afficheront mal.

## Utilisation de Javapass

Une fois l'application lancée, suivez les indications affichées à l'écran. Voici les fonctions principales :

![Ex_1](img/Exemples/1_ex.png)

### 1. Inscription et Connexion
Au lancement, l'écran de bienvenue s'affiche :
- Tapez **[S]** pour créer un compte. **Attention :** Ne perdez surtout pas votre mot de passe maître, c'est la clé de votre coffre-fort !

![Ex_2](img/Exemples/2_ex.png)

- Tapez **[L]** pour vous connecter à un compte existant.

![Ex_3](img/Exemples/3_ex.png)

### 2. Le menu d'accueil
Une fois connecté(e), tapez simplement le numéro de l'action souhaitée puis "Entrée" :
- **[1]** Consulter et gérer vos mots de passe enregistrés.
- **[2]** Ajouter un nouveau mot de passe.
- **[3]** Modifier votre mot de passe maître.
- **[4]** Supprimer définitivement votre compte.
- **[5]** Quitter l'application.

![Ex_4](img/Exemples/4_ex.png)

### 3. Ajouter un nouveau mot de passe
Avec l'option **[2]**, enregistrez les identifiants d'un nouveau site (nom, URL, identifiant).
Pour le mot de passe, vous avez le choix :
- Le taper vous-même.
- Laisser JavaPass en **générer un automatiquement**.

![Ex_5](img/Exemples/5_ex.png)

### 4. Consulter et rechercher
Avec l'option **[1]**, JavaPass liste vos sites enregistrés :
- Tapez le **numéro** d'un site pour en voir les détails.

    ![Ex_8](img/Exemples/8_ex.png)

- Recherchez un site précis en tapant `s*` suivi du nom (ex: `s*google`). La recherche, résistante à la casse, affichera tous les sites contenant votre recherche.
    - Avant recherche :
    
    ![Ex_6](img/Exemples/6_ex.png)

    - Après recherche :

    ![Ex_7](img/Exemples/7_ex.png)

    - **Astuce** : Pour faire en sorte que tous vos mots de passe s'affichent après une recherche, entrez seulement `s*`

- Tapez **[Q]** pour revenir à l'accueil.

### 5. Gérer un mot de passe
En consultant les détails d'un mot de passe, plusieurs actions s'offrent à vous :
- **[1] Analyser :** Vérifie la robustesse du mot de passe et son temps de piratage estimé, alerte l'utilisateur en cas de réutilisation sur d'autres sites et propose de l'améliorer s'il est jugé trop faible.
    - Cas d'un mot de passe fort :

    ![Ex_9](img/Exemples/9_ex.png)

    - Cas d'un mot de passe faible :

    ![Ex_10](img/Exemples/10_ex.png)
- **[2] Modifier :** Remplace l'ancien mot de passe par un nouveau.
- **[3] Supprimer :** Efface complètement ce mot de passe.
- **[4] Retour :** Revient à la liste des sites.
