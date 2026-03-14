# Architecture du Projet JavaPass :

![Image de l'architecture du projet](img/plantUML.png)

## Ressources :

Lien pour visualiser l'architecture : https://editor.plantuml.com

Lien pour connaître la syntaxe UML : https://plantuml.com/fr/class-diagram

## Architecture (à copier coller sur le site)

```
@startuml
class Main
class Interfaces
class Services
class SQLite
class AES
class Argon2
class User

Main ..> Interfaces : crée
Interfaces ..> Services : crée
Interfaces --> Services : utilise
Services --> AES : utilise
Services --> Argon2 : utilise
Services ..> SQLite : initialise
Services --> SQLite : utilise
Services ..> User : crée

Main : main()

Interfaces : afficherBienvenue()
Interfaces : clearScreen()
Interfaces : bandeau()
Interfaces : erreur()
Interfaces : connection()
Interfaces : inscription()
Interfaces : accueil()

Services : connectionDB()
Services : authentification()
Services : inscription()
Services : generatePassword()
Services : researchID()

SQLite : initialistionDB()
SQLite : ajoutTable_base()
SQLite : ajout_utilisateur()
SQLite : ajout_mdp()
SQLite : get_user()
SQLite : get_mdp()

AES : generateIv()
AES : generateGCMParameterSpec()
AES : encrypt()
AES : decrypt()

Argon2 : generateSalt()
Argon2 : derivePassword()

User : getUserId()
User : getKey()
@enduml
```
