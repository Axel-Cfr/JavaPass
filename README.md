# JavaPass
![Java](https://img.shields.io/badge/Java-25-00bf63?style=for-the-badge&logo=openjdk&logoColor=00bf63)
![SQLite](https://img.shields.io/badge/SQLite-3.51.1-4e7896?style=for-the-badge&logo=sqlite&logoColor=4e7896)
![Maven](https://img.shields.io/badge/Maven-3.9.12-00bf63?style=for-the-badge&logo=apachemaven&logoColor=00bf63)

![Logo du projet](docs/img/Javapass.png)

## Description

JavaPass est un gestionnaire de mots de passe en ligne de commande (CLI) local et open source entièrement développé en Java.  
Il fonctionne dans le terminal et stocke vos mots de passe chiffrés dans une base de données SQLite.  
Vos données restent sur votre ordinateur et votre mot de passe maître n'est stocké nulle part, pas même sur votre machine.  
Ce projet a été réalisé dans le cadre du cours de programmation Java en première année du cycle préparatoire intégré de 3iL.

**Plus d'informations dans la [documentation technique](docs/Doc_technique.md)**

## Fonctionnalités

- Stockage sécurisé de vos identifiants et mots de passe avec l'algorithme chiffrement **AES-256-GCM**
- Accès à vos identifiants via un mot de passe maître dérivé avec **Argon2d/id**
    - Ni votre mot de passe maître, ni la clé dérivée ne sont stockés
- Génération de mots de passes sûrs
- Analyse de la robustesse d'un mot de passe
- Renforcement d'un mot de passe existant
- Recherche rapide de vos identifiants
- Fermeture automatique du gestionnaire au bout de **5 min** d'inactivité
- Interface soignée avec de l'ASCII et des codes couleurs ANSI

## Prérequis

[Java](https://adoptium.net/fr/temurin/releases) == 25  
[Git](https://git-scm.com/install) == 2.52.0  
[Apache Maven](https://maven.apache.org/download.cgi) == 3.9.12  

Dépendances gérées par Maven (installées automatiquement) :
- BouncyCastle : bcprov-jdk18on == 1.83  
- SQLite : sqlite-jdbc == 3.51.1.0
- JUnit : junit == 4.13.2

## Installation

1. Téléchargez le dépôt
```
git clone https://github.com/Axel-Cfr/JavaPass.git
```

2. Nettoyez les potentiels fichiers générés par Maven, compilez et installez les fichiers du projet 
```
mvn clean install
```

3. Indiquez à la console d'afficher les caractères avec l'encodage UTF-8
- **Vous pouvez passer cette étape si vous utilisez un système Linux/Unix**
```
C:\Windows\System32\chcp.com 65001
```

4. Lancez JavaPass
```
java -jar target/JavaPass.jar
```

**Plus d'informations dans la [documentation fonctionnelle](docs/Doc_fonctionnelle.md)**

## Auteurs

- [Axel](https://github.com/Axel-Cfr)
- [Morgann](https://github.com/Morgannnnnnnn)
- [Octave](https://github.com/O-glt)

## Liens

- [GitHub](https://github.com/Axel-Cfr/JavaPass)
- [Trello](https://trello.com/b/ZfCDiRwj/javapass)
- [Technical Documentation](docs/Doc_technique.md)
- [Functional Documentation](docs/Doc_fonctionnelle.md)

## Licence

JavaPass est sous licence [GPL v3.0](LICENSE)