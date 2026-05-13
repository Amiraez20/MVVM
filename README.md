# Task Manager App — TP Android MVVM + Room

## Description

Application Android développée dans le cadre d'un TP sur l'architecture **MVVM** (Model-View-ViewModel) avec persistance locale via **Room**. L'objectif est de comprendre la séparation des responsabilités entre les différentes couches d'une application Android moderne.

---

## Architecture du projet

```
com.example.taskmanagerapp
│
├── data
│   ├── local
│   │   ├── TaskEntry.java        # Entité Room (table SQLite)
│   │   ├── TaskDao.java          # Interface d'accès aux données
│   │   └── TaskDatabase.java     # Point central de la base Room
│   └── TaskRepository.java       # Intermédiaire entre ViewModel et DAO
│
├── ui
│   ├── HomeActivity.java         # Vue principale (Activity)
│   └── TaskListAdapter.java      # Adapter RecyclerView
│
└── viewmodel
    └── TaskViewModel.java        # Logique de présentation
```

---

## Rôle de chaque couche

| Couche | Classe | Rôle |
|---|---|---|
| Entity | `TaskEntry` | Définit la structure de la table SQLite |
| DAO | `TaskDao` | Déclare les opérations SQL (insert, delete, select) |
| Database | `TaskDatabase` | Singleton — point d'entrée de Room |
| Repository | `TaskRepository` | Centralise l'accès aux données, gère le thread secondaire |
| ViewModel | `TaskViewModel` | Survit aux rotations, expose les données via LiveData |
| View | `HomeActivity` | Observe le LiveData, délègue les actions au ViewModel |
| Adapter | `TaskListAdapter` | Affiche la liste de tâches dans le RecyclerView |

---

## Fonctionnalités

- Saisie d'un titre et d'une description de tâche
- Ajout d'une tâche avec persistance locale
- Suppression d'une tâche par **clic long**
- Suppression de toutes les tâches
- Liste mise à jour automatiquement via **LiveData**
- Données conservées après fermeture de l'application
- État conservé après **rotation de l'écran** grâce au ViewModel

---

## Stack technique

| Technologie | Version |
|---|---|
| Langage | Java |
| Min SDK | 24 |
| Room | 2.6.1 |
| Lifecycle (ViewModel + LiveData) | 2.8.7 |
| RecyclerView | 1.4.0 |
| CardView | 1.0.0 |

---

## Dépendances (`build.gradle.kts`)

```kotlin
implementation("androidx.room:room-runtime:2.6.1")
annotationProcessor("androidx.room:room-compiler:2.6.1")

implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

implementation("androidx.recyclerview:recyclerview:1.4.0")
implementation("androidx.cardview:cardview:1.0.0")
```

---

## Circulation des données

### Sens descendant (action utilisateur → base)

```
HomeActivity
    └── TaskViewModel.addEntry(entry)
            └── TaskRepository.addEntry(entry)
                    └── TaskDao.addEntry(entry)   [thread secondaire]
                            └── SQLite (Room)
```

### Sens remontant (données → interface)

```
SQLite (Room)
    └── TaskDao.fetchAllEntries()  →  LiveData<List<TaskEntry>>
            └── TaskRepository.getEntryList()
                    └── TaskViewModel.getEntryList()
                            └── HomeActivity.observe()
                                    └── TaskListAdapter.refreshList()
                                            └── RecyclerView
```

---

## Comportement lors de la rotation

Lors d'une rotation d'écran, Android recrée l'`Activity`. Le `TaskViewModel`, associé au cycle de vie logique de l'écran, est conservé à travers ce changement de configuration. Les données observées via `LiveData` sont automatiquement réémises à la nouvelle instance de l'`Activity` sans perte.

---

## Tests réalisés

---

### Test 1 — Insertion simple

**Scénario :** Ajouter deux tâches successives.  
**Résultat attendu :** Les deux tâches s'affichent immédiatement dans la liste.

[Ajout (2).webm](https://github.com/user-attachments/assets/92d6fd6c-983c-4d37-81a5-caa98a967cca)

---

### Test 2 — Suppression par clic long

**Scénario :** Effectuer un clic long sur une tâche dans la liste.  
**Résultat attendu :** La tâche disparaît de la liste.

[suppression.webm](https://github.com/user-attachments/assets/31f3d3dc-0e0e-4a24-95a7-4ba81f7465ac)

---

### Test 3 — Persistance des données

**Scénario :** Fermer complètement l'application puis la rouvrir.  
**Résultat attendu :** Les tâches sont toujours présentes.

[persistance.webm](https://github.com/user-attachments/assets/a4e1df29-f4f3-4a65-9470-a441a3cc4689)


---

### Test 4 — Rotation d'écran

**Scénario :** Ajouter une tâche, puis tourner l'écran.  
**Résultat attendu :** La liste reste cohérente, l'écran se recharge sans perdre les données observées via le ViewModel.

[rotation.webm](https://github.com/user-attachments/assets/40617bb9-09ce-4ce2-a035-8f13c2531691)


---

### Test 5 — Suppression globale

**Scénario :** Cliquer sur le bouton "TOUT EFFACER".  
**Résultat attendu :** La liste devient vide.

[tout_effacer.webm](https://github.com/user-attachments/assets/2794e425-9c7e-43e3-ad22-e6728a7d5e65)

---

## Ce qui a été appris

- **Room** fournit une abstraction solide au-dessus de SQLite avec génération automatique du code d'accès aux données via les annotations `@Entity`, `@Dao` et `@Database`.
- **LiveData** permet d'observer les données en respectant le cycle de vie du composant UI, évitant les mises à jour inutiles quand l'écran est inactif.
- **ViewModel** conserve l'état de l'écran à travers les changements de configuration et encapsule la logique de présentation hors de l'Activity.
- **RecyclerView** affiche efficacement des listes de données en recyclant les vues hors écran.
- Les opérations Room doivent être exécutées hors du thread principal via un `ExecutorService` pour préserver la fluidité de l'interface.
- Sans MVVM, les Activities deviennent rapidement trop volumineuses, difficiles à tester et fragiles face aux changements de configuration.
