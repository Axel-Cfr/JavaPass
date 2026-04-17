# Documentation Fonctionnelle de JavaPass

## Prérequis

- [JDK Java 25](https://adoptium.net/fr/temurin/releases)
- [Maven 3.9.12](https://maven.apache.org/download.cgi)

Si vous n'avez pas configuré Maven, cliquez ici pour voir le [guide de configuration Maven](Tuto_Maven.md)

## Installation et Mise en place

1. Ouvrez l'invite de commande et rendez-vous dans le répertoire où vous voulez que le dépôt soit cloné
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

5. Lancez JavaPass
```
java -jar target/JavaPass.jar
```

6. Optionnel : Vous pouvez créer un fichier JavaPass.bat cliquable en y écrivant ceci dans un fichier texte et l'enregistrant sous forme de fichier .bat :
```
cd C:/Users/votre_user/chemin/vers/JavaPass
java -jar target/JavaPass.jar
```

7. Optionnel : Vous pouvez créer un raccourci de JavaPass.bat et lui attribuer l'icône de JavaPass. Le fichier Javapass.ico se trouve dans `C:/Users/votre_user/chemin/vers/JavaPass/docs/img`

## Utilisation de Javapass

Une fois l'application lancée, suivez les indications affichées à l'écran. Voici les fonctions principales :

### 1. Inscription et Connexion
Au lancement, l'écran de bienvenue s'affiche :
- Tapez **[S]** pour créer un compte. **Attention :** Ne perdez surtout pas votre mot de passe maître, c'est la clé de votre coffre-fort !
- Tapez **[L]** pour vous connecter à un compte existant.

### 2. Le menu d'accueil
Une fois connecté(e), tapez simplement le numéro de l'action souhaitée puis "Entrée" :
- **[1]** Consulter et gérer vos mots de passe enregistrés.
- **[2]** Ajouter un nouveau mot de passe.
- **[3]** Modifier votre mot de passe maître.
- **[4]** Supprimer définitivement votre compte.
- **[5]** Quitter l'application.

### 3. Ajouter un nouveau mot de passe
Avec l'option **[2]**, enregistrez les identifiants d'un nouveau site (nom, URL, identifiant).
Pour le mot de passe, vous avez le choix :
- Le taper vous-même.
- Laissez JavaPass en **générer un automatiquement**.

### 4. Consulter et rechercher
Avec l'option **[1]**, JavaPass liste vos sites enregistrés :
- Tapez le **numéro** d'un site pour en voir les détails.
- Recherchez un site précis en tapant `s*` suivi du nom (ex: `s*google`).
- Tapez **[Q]** ou **[R]** pour revenir à l'accueil.

### 5. Gérer un mot de passe
En consultant les détails d'un mot de passe, plusieurs actions s'offrent à vous :
- **[1] Analyser :** Vérifie la robustesse du mot de passe et son temps de piratage estimé. Alerte en cas de réutilisation sur d'autres sites, et propose de l'améliorer s'il est jugé trop faible.
- **[2] Modifier :** Remplace l'ancien mot de passe par un nouveau.
- **[3] Supprimer :** Efface complètement ce mot de passe.
- **[4] Retour :** Revient à la liste des sites.