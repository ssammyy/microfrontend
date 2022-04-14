# Reports
+ Background color: #D9E1F2
# Run application
```bash
java -Dlogging.path=/app/apps_home/apolo/api/logs -Dserver.port=8005 -server -Xms2g -Xmx2g -server -XX:-UsePerfData -jar -Dspring.profiles.active=dev apollo-api/build/libs/apollo-api-2.0-SNAPSHOT.war
```

# Issues
1. Cannot connect/login to: sftp://kebs\bsk@10.10.0.127:922. Will skip this poll.: Login to sftp server manually before starting the application: https://access.redhat.com/solutions/2071143
```
ssh-keyscan -t rsa -p 922 10.10.0.127 >>~/.ssh/known_hosts
```