# Librairie json-datafile

L'objectif de la librairie `json-datafile` est de proposer une micro BDD au format fichier JSON.
L'idée est de pouvoir implémenter rapidement une couche de stockage dans une appli sans avoir à se brancher/créer une
vraie BDD.


## :scroll: How to Use

#### Import de la librairie

Dépendance Maven :

```xml

<dependency>
    <groupId>fr.forge.lib</groupId>
    <artifactId>json-datafile</artifactId>
    <version>2.0.0</version>
</dependency>
```

#### Interface Database

Exemple d'utilisation de l'inteface `Database` :

```java
public class Service {

    private final Database<Model> modelDatabase;
    private Model model;

    /**
     * Initialisation d'une database pour l'objet Model
     */
    public Service() {
        this.modelDatabase = new JsonDatabase<>(Model.class, "fs/database/model.json");
        this.model = this.loadDatabase();
    }

    /**
     * Chargement de l'objet Model depuis la database
     */
    private Model loadDatabase() {
        try {
            return modelDatabase.load();
        } catch (JsonDatabaseException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Mise à jour de l'objet Model dans la database
     */
    private void syncDatabase() {
        try {
            this.modelDatabase.save(this.model);
        } catch (JsonDatabaseException e) {
            log.error(e.getMessage());
        }
    }
}
```


## :clipboard: Documentation interne

#### Publication de l'artifact dans le repo Github Packages

Le pipeline github actions est dispo ici : `.github/workflows/publish-artifact.yml`

Overview :

- Build java via `maven deploy`
- Nom de l'image : `fr.forge.lib.json-datafile`

#### Déploiement manuel de l'artifact sur Github Packages

```
# Set version to release version
mvn versions:set -DnewVersion=1.x

# Deploy to package registry
mvn clean deploy

# Set version to next snapshot version
mvn versions:set -DnewVersion=1.(x+1)-SNAPSHOT
```
