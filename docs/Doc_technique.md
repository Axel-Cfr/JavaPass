# Documentation Technique de JavaPass

## Table des matières

- [Architecture](#architecture)
- [Sécurité](#sécurité)
    - [Chiffrement](#chiffrement)
    - [Hachage](#hachage)
    - [Bonnes pratqies](#bonnes-pratiques)
- [Base de données](#base-de-données)
- [Fonctionnalités](#fonctionnalités)
- [Gestion des Dépendances](#gestion-des-dépendances)
- [Source]()

## Architecture

![Image de l'architecture du projet](img/plantUML.png)

Ce projet possède une architecture en couches qui se distingue en 3 couches :
- Interface utilisateur : gérée par la classe `Interface`, responsable de l'affichage et les interactions avec utilisateur
- Logique métier : gérée par la classe `Services`, qui assure les différentes fonctionnalités du gestionnaire de mot de passe, délègue le chiffrement à la classe `AES`, le hachage à `Argon2`, la communication avec la base de données à `SQLite`, instancie et utilise un objet de la classe `User` et gère les erreurs (sauf les erreurs de scanners qui sont gérées par `Interface`)
- Base de données : gérée par la classe `SQLite`, qui communique avec la base de donnée et renvoie les informations demandées

La classe Main instancie un objet de la classe `Services` et un de la classe `Interface`  

L'architecture en couches est une architecture très classique pour créer des applications de bureaux. Elle permet de séparer les responsabilités; chaque couche ne peut communiquer qu'avec celle qui se trouve directement en dessous. Cela assure une certaine évolutivité et la lisibilité du code tout en restant une architecture adaptée à un projet académique (utiliser des architectures plus complexes ne serait d'aucune utilité dans ce cas de figure).

## Sécurité

### Chiffrement

L'algorithme de chiffrement utilisé dans JavaPass est l'AES-256-GCM.

### Hachage

### Bonnes pratiques

## Base de données

![Image de la BDD](img/DBdiagram.png)

## Fonctionnalités

## Gestion des dépendances

## Sources

- Architecture
    - https://www.redhat.com/fr/topics/cloud-native-apps/what-is-an-application-architecture
- AES
    - https://www.youtube.com/watch?v=5ZEYKk8BHcE
    - https://www.developpez.net/forums/blogs/863457-autran/b1016/chiffrement-aes-java/
    - https://www.remipoignon.fr/aes-le-big-boss-du-chiffrement-symetrique/
    - https://owasp.org/www-community/Using_the_Java_Cryptographic_Extensions
    - https://jmdoudoux.developpez.com/cours/developpons/java/chap-jce.php
    - https://www.baeldung.com/java-aes-encryption-decryption
    - https://fr.wikipedia.org/wiki/Advanced_Encryption_Standard
- Argon2
    - https://www.baeldung.com/java-argon2-hashing
    - https://www.rfc-editor.org/rfc/rfc9106.html
    - https://argon2-cffi.readthedocs.io/en/stable/api.html
    - https://argon2-cffi.readthedocs.io/en/stable/parameters.html
    - https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
    - https://app.itsasync.fr/post/async-article-1010
    - https://tuta.com/fr/blog/best-encryption-with-kdf
- SQLite pour Java
    - https://sqlite.fr/languages/java/
    - https://www.sqlitetutorial.net/sqlite-java/jdbc-read-write-blob/
    - https://sqlite.org/datatype3.html
    - https://github.com/xerial/sqlite-jdbc/blob/master/USAGE.md
    - https://www.datacamp.com/fr/tutorial/sqlite-data-types
- Maven
    - https://maven.apache.org/download.cgi
    - https://www.youtube.com/watch?v=Aaq3FaadNQo
    - https://objis.com/tutoriel-maven-n1-installation-et-phases/


