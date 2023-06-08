# Reports
+ Reports Background color: #D9E1F2
+ KEBs Light Blue: #CAE3FA
# Run application
```bash
java -Dlogging.path=/app/apps_home/apolo/api/logs -Dserver.port=8005 -server -Xms2g -Xmx2g -server -XX:-UsePerfData -jar -Dspring.profiles.active=dev apollo-api/build/libs/apollo-api-2.0-SNAPSHOT.war
```

# VPN
+ Download snx_install: https://supportcenter.checkpoint.com/supportcenter/portal/user/anon/page/default.psml/media-type/html?action=portlets.DCFileAction&eventSubmit_doGetdcdetails=&fileid=22824
```
sudo apt-get install libstdc++5:i386 libx11-6:i386 libpam0g:i386
wget https://vpnportal.aktifbank.com.tr/SNX/INSTALL/snx_install.sh --no-check-certificate
ls
```

# Issues
1. Cannot connect/login to: sftp://kebs\bsk@10.10.0.127:922. Will skip this poll.: Login to sftp server manually before starting the application: https://access.redhat.com/solutions/2071143
```
ssh-keyscan -t rsa -p 922 10.10.0.127 >>~/.ssh/known_hosts
```
2. export NODE_OPTIONS="--max-old-space-size=8192"
```
export NODE_OPTIONS="--max-old-space-size=8192"
```
or