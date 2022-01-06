### Steps To Execute

```sh
mvn clean test -Dbrowser=chrome 
mvn clean test -Dbrowser=firefox
```

To set profile

```sh
mvn clean test -Dbrowser=chrome -P prod
```
* Default profile is dev 
* Default browser is Chrome

