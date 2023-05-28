# Library json-datafile

## :scroll: How to Use

TODO

## :clipboard: Internal documentation

#### :package: Deploy artifact to Github Packages

```
# Set version to release version
mvn versions:set -DnewVersion=1.x

# Deploy to package registry
mvn clean deploy

# Set version to next snapshot version
mvn versions:set -DnewVersion=1.(x+1)-SNAPSHOT
```
