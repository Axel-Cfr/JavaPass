# JavaPass
![Java](https://img.shields.io/badge/Java-25-00bf63?style=for-the-badge&logo=openjdk&logoColor=00bf63)
![SQLite](https://img.shields.io/badge/SQLite-3.51.1-4e7896?style=for-the-badge&logo=sqlite&logoColor=4e7896)
![Maven](https://img.shields.io/badge/Maven-3.9.12-00bf63?style=for-the-badge&logo=apachemaven&logoColor=00bf63)

![Logo du projet](docs/img/Javapass.png)

## Description

JavaPass est un gestionnaire de mots de passe local et open source entièrement développé en Java.  
Il fonctionne dans le terminal et stocke vos mots de passe chiffrés dans une base de données SQLite.  
Vos données restent sur votre ordinateur et votre mot de passe maître n'est stocké nulle part, pas même sur votre machine.  
Ce projet a été réalisé dans le cadre du cours de programmation Java en première année du cycle préparatoire integré de 3iL.

## Features

- Stockage de manière sécurisée vos identifiants et mots de passe en les chiffrant avec AES-256-GCM
- Accès à vos identifiants via un mot de passe maître dérivé avec Argon2d/id
    - Ni votre mot de passe maître, ni la clé dérivée ne sont stockés
- Génération des mots de passes sûrs
- Analyse de la robustesse d'un mot de passe
- Renforcement d'un mot de passe existant
- Recherche rapide de vos identifiants

## Requirements

Java == 25   
Apache Maven == 3.9.12  

Dépendances gérées par Maven :
- BouncyCastle : bcprov-jdk18on == 1.83  
- SQLite : sqlite-jdbc == 3.51.1.0
- JUnit : junit == 4.13.2

## Authors

- [Axel](https://github.com/Axel-Cfr)
- [Morgann](https://github.com/Morgannnnnnnn)
- [Octave](https://github.com/O-glt)

## Links

- [GitHub](https://github.com/Axel-Cfr/JavaPass)
- [Trello](https://trello.com/b/ZfCDiRwj/javapass)
- [Technical Documentation](docs/Doc_technique.md)
- [Functional Documentation](docs/Doc_fonctionnelle.md)

## License

JavaPass est sous license [GPL v3.0](LICENSE)