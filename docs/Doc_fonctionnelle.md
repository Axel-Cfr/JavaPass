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