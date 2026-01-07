# Smart Attendance System — Guide d'exécution (Windows)

## Lancement rapide

Dans le dossier du projet:

mvn javafx:run

## Sommaire

1. Prérequis
2. Vérifications rapides
3. Exécution recommandée (Maven + JavaFX)
4. Exécution depuis l'IDE
5. Build/Packaging
6. Dépannage
7. Commandes utiles
8. Informations techniques
9. Structure du projet
10. Charger des données de test (Load Test Data)
11. Import de données CSV
12. Version Étudiant (auto‑inscription)

13) Prérequis

---

- Système: Windows 10/11 (64‑bit)
- Java (JDK): 21 (recommandé pour compiler/faire tourner ce projet)
  - Assurez-vous que JAVA_HOME pointe vers un JDK 21 et que « java -version » affiche 21
- Maven: 3.8+ (recommandé 3.9.x)
- Internet: nécessaire au premier build pour télécharger les dépendances
- Firebase/Firestore: un compte de service JSON valide
  - Placez le fichier: src/main/resources/serviceAccountKey.json
  - Chargé automatiquement via le classpath (aucune variable d'environnement requise)

2. Vérifications rapides

---

Exécutez ces commandes dans un terminal ouvert à la racine du projet (où se trouve pom.xml):

java -version
mvn -version

3. Exécution recommandée (Maven + JavaFX)

---

Utilise le plugin JavaFX pour gérer automatiquement les modules JavaFX sur Windows.

mvn javafx:run

Options:
-DskipTests (accélère le lancement)
-Dprism.order=sw (si souci d'affichage GPU)

Exemples:
mvn -DskipTests javafx:run
mvn -DskipTests -Dprism.order=sw javafx:run

4. Exécution depuis l'IDE

---

- Ouvrez le projet Maven.
- Lancez la classe principale: org.attendanceApp.application.Main (Application JavaFX)

5. Build/Packaging

---

Construire un JAR « fat »:

mvn clean package

Artefacts (dans target/):

- application-1.0-SNAPSHOT.jar (JAR ombré via maven-shade-plugin)

Remarque:

- Le manifest du JAR référence org.attendanceApp.application.Launcher.
  Si cette classe n'existe pas, préférez: mvn javafx:run, ou lancez Main depuis l'IDE.

6. Dépannage

---

- « Resource not found: serviceAccountKey.json »
  Assurez-vous que src/main/resources/serviceAccountKey.json existe et est lisible.

- Problèmes JavaFX (écran blanc, Toolkit, modules manquants)
  Lancez via: mvn javafx:run (gère les libs natives et modules pour Windows).

- Version Java incompatible
  Utilisez Java 21. Si votre pom.xml cible une autre version, ajustez maven.compiler.source/target à 21.

- Problèmes réseau Maven (proxy)
  Réessayez ou configurez le proxy dans ~/.m2/settings.xml.

7. Commandes utiles

---

- Lancer rapidement l'app:
  mvn -DskipTests javafx:run

- Build sans tests:
  mvn -DskipTests package

- Nettoyer:
  mvn clean

- Option d'affichage software (si souci GPU JavaFX):
  mvn -DskipTests -Dprism.order=sw javafx:run

8. Informations techniques

---

- Java recommandé: 21
- Classe principale (dev): org.attendanceApp.application.Main
- Plugins Maven clés:
  - org.openjfx:javafx-maven-plugin → exécution dev (javafx:run)
  - maven-shade-plugin → JAR autonome

9. Structure du projet (extraits)

---

- Sources: src/main/java/org/attendanceApp/application/...
- Ressources: src/main/resources/ (inclut serviceAccountKey.json, styles.css)
- Build: pom.xml
- Sorties: target/

10. Charger des données de test (Load Test Data)

---

Pour tester rapidement l'application sans créer manuellement des étudiants:

1. Dans l'application:

   - Cliquez sur le bouton « Load Test Data » (Charger données de test) dans le panneau de contrôle.

2. L'application charge automatiquement:

   - Une classe 3x4 (3 lignes, 4 colonnes)
   - 7 étudiants de test avec positions prédéfinies:
     - Fatima, Ahmed, Mohamed, Hanane, Sara (présents)
     - Youssef (absent - sans position)
     - Yassine (avec déclarations incomplètes/conflictuelles)
   - Déclarations (claims) pré-remplies pour démontrer les vérifications

3. Utilité:

   - Test rapide du système de vérification d'assiduité
   - Démonstration des différents statuts (PRÉSENT, ABSENT, INCERTAIN)
   - Validation des détections de conflits et menteurs

4. Après chargement:

   - Les étudiants apparaissent dans la grille
   - Vous pouvez lancer « Analyser l'assiduité » pour voir les résultats
   - Statut affiche: "✓ Test data loaded: 7 students..."

5. Import de données CSV

---

Étapes pour importer des étudiants depuis un fichier CSV:

1. Préparez le fichier CSV avec les en-têtes suivants (obligatoires: id, name):

   Exemple CSV:
   id,name,row,col
   1,Yassine,0,0
   2,Ahmed,0,1
   3,Hajar,1,0

   Champs supportés:

   - id (obligatoire): identifiant unique étudiant
   - name (obligatoire): nom de l'étudiant
   - row (optionnel): numéro de ligne (0-indexed)
   - col (optionnel): numéro de colonne (0-indexed)

2. Dans l'application:

   - Cliquez sur le bouton « Importer CSV » dans le panneau de contrôle.
   - Sélectionnez votre fichier CSV.
   - L'application:
     - Détecte automatiquement les dimensions de la classe (max row + max col).
     - Crée la grille classroom avec ces dimensions.
     - Place les étudiants aux positions spécifiées.
     - Affiche un avertissement si des doublons (ID ou nom) sont détectés.

3. Résultat:
   - Les étudiants importés apparaissent dans la liste et la grille.
   - Statut affiche: "✓ Imported X students from CSV"
   - Vous pouvez ensuite ajouter des déclarations (claims) manuellement ou continuer.

Dépannage CSV:

- « CSV must have at least 'id' and 'name' headers »
  Ajoutez les colonnes obligatoires au fichier CSV.

- « No valid students found »
  Vérifiez que les lignes contiennent des valeurs non-vides pour id et name.

- « Duplicate students detected »
  Chaque étudiant doit avoir un ID et un nom uniques.

12. Version Étudiant (auto‑inscription)

---

Objectif: permettre aux étudiants d'ajouter leurs informations avant l'import par l'enseignant.

Flux général:

1. Enseignant: définit lignes/colonnes, clique « Créer », puis « Configurer » (Setup Firestore).
2. Étudiants (application Étudiant): saisissent leurs données dans le même projet Firestore.
3. Enseignant: clique « Importer les données » → ferme l'inscription et charge la classe.

Côté Enseignant (dans cette application):

- Bouton « Configurer » (Setup):

  - Crée/actualise classroom_setup/current_session avec status=active, setupTime, teacherId, classroomSize.
  - Vide la collection students (remise à zéro optionnelle) pour cette session.
  - Active le bouton « Importer les données ».

- Bouton « Importer les données »:
  - Passe status=closed (ferme l'inscription), importe les étudiants/positions/claims.
  - Propose d'effacer les enregistrements côté cloud (optionnel) puis réinitialise l'état de setup.

Côté Étudiant (application externe):

- Utiliser la configuration Firebase côté client (web/mobile/desktop) vers le même Firestore.
- Autoriser l'inscription uniquement si classroom_setup/current_session.status == "active".
- Écrire un document dans la collection students avec les champs:
  - studentId: String
  - name: String
  - row: Number (optionnel)
  - col: Number (optionnel)
  - claims (ou neighborClaims): Array d'objets
    { direction: "LEFT|RIGHT|FRONT|BACK", student: null | { studentId: String, name: String } }

Collections Firestore utilisées:

- classroom_setup/current_session
  { status: "active"|"closed", setupTime, closeTime?, teacherId, classroomSize }
- students
  1 document par étudiant (schéma ci‑dessus)
