# Tutoriel : Installer et configurer Maven pour JavaPass

## Etape 1

Télécharger et extraire le .zip de Maven dans le répertoire que vous souhaitez.
Lien du téléchargement : https://maven.apache.org/download.cgi

![1_Maven](img/Maven/1_Maven.png)

L’intérieur du répertoire Maven ressemble à cela :

![2_Maven](img/Maven/2_Maven.png)

## Etape 2

Se rendre dans les propriétés système puis dans les variables d’environnement.

![3_Maven](img/Maven/3_Maven.png)

![4_Maven](img/Maven/4_Maven.png)

## Etape 3

Ajouter la variable d’environnement utilisateur MAVEN_HOME avec pour valeur le chemin du répertoire apache-maven-3.9.12

![5_Maven](img/Maven/5_Maven.png)

## Etape 4

Ajouter aussi la variable d’environnement MAVEN, avec comme valeur le chemin du répertoire apache-maven-3.9.12/bin

![6_Maven](img/Maven/6_Maven.png)

## Etape 5
Créer ou modifier la variable d’environnement PATH avec comme valeur le chemin du répertoire apache-maven-3.9.12; et le chemin du répertoire \bin de votre JDK (penser au séparateur ';')

![7_Maven](img/Maven/7_Maven.png)

## Etape 6

Pour vérifier que tout est en place, exécuter mvn --version dans un terminal

![8_Maven](img/Maven/8_Maven.png)

## Etape 7

Enfin exécuter cd le\chemin\du\répertoire\où\se\trouve\JavaPass, puis mvn clean install

![9_Maven](img/Maven/9_Maven.png)

## Etape 8

Profiter pleinement de son Maven configuré !
