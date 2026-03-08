# Architecture SQLIte avec 2 tables principales :

![Image de la BDD](img\DBdiagram.png)

## Ressources :

Lien pour visualiser l'architecture : https://dbdiagram.io/d

Lien pour connaître la syntaxe du site : https://dbml.dbdiagram.io/docs/

## Architecture (à copier coller sur le site)

### Table users
```
Table users {
  user_id integer [primary key, increment] //autoincrement
  username text [unique, not null]
  encrypted_textAndTag_verify text [not null]
  iv_verify blob [not null]
  salt blob [not null]
  last_login text [not null]
}
```

### Table passwords
```
Table passwords {
  password_id integer [primary key, increment] //autoincrement
  user_id integer [not null, ref: > users.user_id]
  website_name text [not null]
  url text //optionnelle
  encrypted_username text [not null]
  encrypted_password text [not null]
  iv_username blob [not null]
  iv_password blob [not null]
}
```

## Requêtes SQL pour créer ces tables
```
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    encrypted_textAndTag_verify BLOB NOT NULL,    //Texte crypté pour vérifier le mdp
    iv_verify BLOB NOT NULL,                      //Vecteur d'initialisation de connection
    salt BLOB NOT NULL,                           //Sel pour la dérivation de clé
    last_login TEXT
);

CREATE TABLE IF NOT EXISTS passwords (
    password_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    website_name TEXT NOT NULL,             // Ex: "Gmail", "Facebook"
    url TEXT,                               // URL optionnelle du site
    encrypted_username TEXT NOT NULL,     // Nom d'utilisateur/email chiffré en tableau d'octets
    encrypted_password TEXT NOT NULL,     // Mot de passe chiffré en tableau d'octets
    iv_username BLOB NOT NULL,                // IV pour le déchiffrement du nom d'utilisateur
    iv_password BLOB NOT NULL,                // IV pour le déchiffrement du mot de passe


    -- En option
    /*category TEXT,                         // Ex: "Social", "Banque", "Travail"
    notes TEXT,                                     // Notes optionnelles chiffrées
    created_at TEXT,
    updated_at TEXT,
    *\
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```