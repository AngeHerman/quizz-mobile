# Quiz Game Mobile (Android)

Bienvenue dans le projet GitHub de notre jeu de quiz mobile en Kotlin. Ce jeu vous permet de tester vos connaissances sur différents sujets en répondant à des questions à choix multiple (QCM) ou ouvertes.

## Fonctionnalités

- **Création de sujets** : Créez des sujets de quiz manuellement ou via un fichier XML.
- **Ajout de questions** : Ajoutez plusieurs questions et réponses pour chaque sujet. Les questions peuvent être de type QCM ou ouvertes.
- **Sélection du sujet** : Choisissez un sujet et lancez une partie de 20 questions.
- **Score** : Obtenez un score à la fin de chaque partie et visualisez votre score global sur la page d'accueil.
- **Niveaux de questions** : Les questions sont classées par niveaux. Les questions de niveau 1 apparaissent chaque jour, celles de niveau 2 tous les 2 jours, etc. Réussir une question augmente son niveau, tandis qu'échouer diminue son niveau.
- **Notifications personnalisées** : Recevez des notifications quotidiennes à l'heure de votre choix pour vous rappeler de jouer.

## Installation

1. Clonez le dépôt :
    ```sh
    git clone https://github.com/votre-utilisateur/votre-depot.git
    ```
2. Ouvrez le projet dans Android Studio.
3. Compilez et exécutez l'application sur votre émulateur ou appareil Android.

## Utilisation

### Création de sujets et ajout de questions

1. **Manuellement** :
    - Allez dans la section "Créer un sujet".
    - Entrez le nom du sujet et ajoutez des questions et réponses.
    
2. **Via fichier XML** :
    - Préparez un fichier XML avec la structure appropriée pour les sujets et les questions.
    - Importez le fichier XML via l'option "Importer un fichier XML" comme: [exemple](https://raw.githubusercontent.com/AngeHerman/docs/main/questionFr.xml).

### Jouer

1. Choisissez un sujet dans la liste des sujets disponibles.
2. Lancez une partie de 20 questions.
3. Répondez aux questions et obtenez votre score à la fin de la partie.

### Notifications

- Configurez l'heure à laquelle vous souhaitez recevoir des notifications quotidiennes pour jouer.
- Recevez des rappels personnalisés à l'heure définie.

## Licence

Ce projet est sous la [GNU General Public License v3.0 (GPL v3)](LICENSE). Pour plus de détails, veuillez consulter le fichier `LICENSE` dans ce répertoire.

## Auteurs
- **Ange Herman KOUE-HEMAZRO**
- **Eric NZABA**
