# Architecture avec 2 tables principales :

## Ressources :

Lien pour visualiser l'architecture : 
https://dbdiagram.io/d

Lien pour connaître la syntaxe du site : https://dbml.dbdiagram.io/docs/

## Architecture (à copier coller sur le site)

### Table users
```
Table users {
  user_id integer [primary key, increment] //autoincrement
  username varchar(100) [unique, not null]
  encrypted_textAndTag_verify varbinary(128) [not null]
  iv_verify binary(12) [not null]
  salt binary(16) [not null]
  last_login TIMESTAMP [not null]
}
```

### Table passwords
```
Table passwords {
  password_id integer [primary key, increment] //autoincrement
  user_id integer [not null, ref: > users.user_id]
  website_name varchar(100) [not null]
  url varchar(100) //optionnelle
  encrypted_username varbinary(128) [not null]
  encrypted_password varbinary(128) [not null]
  iv_username binary(12) [not null]
  iv_password binary(12) [not null]
}
```

## Requêtes SQL pour créer ces tables
```
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    encrypted_textAndTag_verify VARBINARY(128) NOT NULL,    //Texte crypté pour vérifier le mdp
    iv_verify BINARY(12) NOT NULL,                          //Vecteur d'initialisation de connection
    salt BINARY(16) NOT NULL,                               //Sel pour la dérivation de clé
    last_login TIMESTAMP
);

CREATE TABLE passwords (
    password_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    website_name VARCHAR(100) NOT NULL,             // Ex: "Gmail", "Facebook"
    url VARCHAR(100),                               // URL optionnelle du site
    encrypted_username VARBINARY(128) NOT NULL,     // Nom d'utilisateur/email chiffré en tableau d'octets
    encrypted_password VARBINARY(128) NOT NULL,     // Mot de passe chiffré en tableau d'octets
    iv_username binary(12) NOT NULL,                // IV pour le déchiffrement du nom d'utilisateur
    iv_password binary(12) NOT NULL,                // IV pour le déchiffrement du mot de passe


    -- En option
    /*category VARCHAR(50),                         // Ex: "Social", "Banque", "Travail"
    notes TEXT,                                     // Notes optionnelles chiffrées
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    *\
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```