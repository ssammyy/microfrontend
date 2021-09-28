# Run application
```bash
java -Dlogging.path=/app/apps_home/apolo/api/logs -Dserver.port=8005 -server -Xms2g -Xmx2g -server -XX:-UsePerfData -jar -Dspring.profiles.active=dev apollo-api/build/libs/apollo-api-2.0-SNAPSHOT.war
```