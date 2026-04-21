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
Main ..> Services : crée
Interfaces --> Services : utilise
Services --> AES : utilise
Services --> Argon2 : utilise
Services ..> SQLite : initialise
Services --> SQLite : utilise
Services --> User : utilise
Services ..> User : crée

Main : main()

Interfaces : afficherBienvenue()
Interfaces : clearScreen()
Interfaces : bandeau()
Interfaces : erreur()
Interfaces : connection()
Interfaces : inscription()
Interfaces : accueil()
Interfaces : voirListeMDP()
Interfaces : voirMDP()
Interfaces : AjouterMDP()
Interfaces : quitter()

Services : connectionDB()
Services : authentification()
Services : inscription()
Services : returnWebsiteName()
Services : researchWebsiteName()
Services : givePasswordInfos()
Services : addNewPassword()
Services : deletePassword()
Services : deleteAccount()
Services : generateSecureRandom()
Services : generatePassword()
Services : check()
Services : enhancePassword()
Services : analysePassword()
Services : convertirDuree()
Services : estFaible()
Services : samePassword()
Services : wait()

SQLite : initialistionDB()
SQLite : deconnexion()
SQLite : ajoutTable_base()
SQLite : ajout_utilisateur()
SQLite : ajout_mdp()
SQLite : get_user()
SQLite : UserValues()
SQLite : get_mdp()
SQLite : MdpValues()
SQLite : suppr_utilisateur()
SQLite : suppr_mdp()
SQLite : get_usernameList()
SQLite : update_sitemdp()
SQLite : update_last_login()

AES : generateIv()
AES : generateGCMParameterSpec()
AES : encrypt()
AES : decrypt()

Argon2 : generateSalt()
Argon2 : derivePassword()

User : User()
User : getUserID()
User : getUsername()
User : getKey()
User : getLast_login()
User : getPasswordIndice()
User : getWebsiteName()
User : getUrl()
User : getEncryptedUsername()
User : getEncryptedPassword()
User : getIvUsername()
User : getIvPassword()
User : getWebsiteNameList()
@enduml
```
