# 🧠 MY_MARVIN — Jenkins as Code (T-DOP-602)

Projet Jenkins automatisé avec Docker, Job DSL et Configuration-as-Code (JCasc).  
Ce dépôt permet de déployer une instance Jenkins entièrement configurée avec les utilisateurs, rôles et jobs nécessaires dès le démarrage.

## 📦 Contenu du projet

```
.
├── Dockerfile          # Image Jenkins personnalisée avec plugins
├── docker-compose.yml  # Lancement de l'instance Jenkins
├── plugins.txt         # Liste des plugins installés au build
├── my_marvin.yml       # Configuration JCasc de Jenkins
├── job_dsl.groovy      # Script Job DSL pour les jobs Tools/*
└── Tools/
    └── README.md       # Placeholder de dossier demandé par le sujet
```

## ⚙️ Lancer le projet

### 1. Variables d’environnement requises

Créez un fichier `.env` ou passez ces variables au moment du lancement :

```env
USER_CHOCOLATEEN_PASSWORD=supersecret1
USER_VAUGIE_G_PASSWORD=supersecret2
USER_I_DONT_KNOW_PASSWORD=supersecret3
USER_NASSO_PASSWORD=supersecret4
```

### 2. Build & Run Jenkins

```bash
docker-compose down -v  # (Optionnel) Nettoie les volumes et instances
docker-compose up --build
```

Jenkins sera accessible sur : [http://localhost:8080](http://localhost:8080)

### 🔐 Utilisateurs créés automatiquement

| ID             | Nom       | Rôle     | Mot de passe                  |
|----------------|-----------|----------|-------------------------------|
| chocolateen    | Hugo      | admin    | `${USER_CHOCOLATEEN_PASSWORD}` |
| vaugie_g       | Garance   | gorilla  | `${USER_VAUGIE_G_PASSWORD}`    |
| i_dont_know    | Jeremy    | ape      | `${USER_I_DONT_KNOW_PASSWORD}` |
| nasso          | Nassim    | assist   | `${USER_NASSO_PASSWORD}`       |

### 🛠️ Jobs Jenkins créés

Tous les jobs se trouvent dans le dossier `Tools/` de Jenkins :

- **✅ clone-repository**  
  **Paramètre** : `GIT_REPOSITORY_URL`  
  Clone un dépôt Git dans l’environnement Jenkins.

- **✅ SEED**  
  **Paramètres** : `GITHUB_NAME`, `DISPLAY_NAME`  
  Utilise le DSL `job_dsl.groovy` pour créer dynamiquement un job :  
  - Clonage Git du dépôt  
  - Déclenchement via SCM poll toutes les minutes (`* * * * *`)  
  - Exécution de la suite Makefile :  
    ```bash
    make fclean
    make
    make tests_run
    make clean
    ```

### 🔒 Rôles & Permissions

Le projet utilise le plugin `role-strategy` avec les rôles suivants :

| Rôle      | Description                                      | Permissions principales       |
|-----------|--------------------------------------------------|--------------------------------|
| admin     | Marvin master                                   | Overall/Administer            |
| ape       | Membre de l’équipe pédagogique                  | Job/Build, Job/Workspace      |
| gorilla   | Groupe de recherche sur l’innovation pédagogique | Job full access               |
