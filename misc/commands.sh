#
#
# $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
# $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
# $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
# $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
# $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
# $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
# $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
# \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
# $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
# \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
#    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
#    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
#    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
#    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
#    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
#    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
#
#
#
#
#
#   Copyright (c) 2020.  BSK
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#

mvn archetype:generate -D archetypeGroupId=org.jetbrains.kotlin -D archetypeArtifactId=kotlin-archetype-jvm -D archetypeVersion=1.3.70 -D groupId=org.kebs.app.kotlin.apollo.auth -D artifactId=apollo-auth -D version=1.0-SNAPSHOT -B

curl -H "Authorization: Basic $(echo -n 7yv3dpvv2dmprzll:wndkuuaf-j7lt3fbfbjobabbiur7tfa3 | base64)" http://41.209.55.98/api/businesses?registration_number=%20CPR%2F2011%2F53201%20

cat tt.txt | sed -e "s/>.*//g" | sed "s/>/\=/g" | sed -e "s/\./_/g" | sed -E 's/_(.)/\U\1/g' | sed -e "s/^</ext./g" | sed -e "s/$/\=/g"

sed -e "s/^$//g" pom.xml

echo "var kafkaPartitionId: Int = 0" | sed -e 's/var //g' | sed -e 's/\:.*//g' | sed -e 's/\=.*//g' | sed -r 's/([A-Z])/_\L\1/g' | sed 's/^_//'

mvn org.springframework.boot:spring-boot-maven-plugin:run -DskipTests=true -e -pl apollo-config

mvn -Dtest="org.kebs.app.kotlin.apollo.config.security.HashingImplementationTest#hashString" test
mvn -DCONFIG_PATH=conf\test -Dtest="org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.IpcAdaptorApplicationTest#objectMapperTest" test
mvn -D"CONFIG_PATH=/home/thor/Desktop/apollo/conf/test" -Dtest="org.kebs.app.kotlin.apollo.ipc.IpcAdaptorApplicationTest#objectMapperTest" test
mvn -D"CONFIG_PATH=/home/thor/Desktop/apollo/conf/test" -Dtest="org.kebs.app.kotlin.apollo.store.StoreApplicationTest#contextLoads" test

paste prefix.txt --delimiter=' ' prefix.txt suffix.txt >merged.txt

mvn -D"CONFIG_PATH=/home/thor/Desktop/apollo/conf/test" '-Dcurrent.project.version=1.0-SNAPSHOT' clean install

mvn clean verify -D"CONFIG_PATH=/home/thor/Desktop/apollo/conf/test" '-Dcurrent.project.version=1.0-SNAPSHOT'

for i in $(find . -name '*.kt'); do
  echo "Replacing on $i"
  sed -i "s/\@Repository/\/\/\@Repository/g" $i
done

for i in $(find . -name '*.kt'); do
  echo "Replacing on $i"
  sed -i "s/\, catalog \= \"\"//g" $i
done

for i in $(find . -name '*.kt'); do
  echo "Replacing on $i"
  sed -i "s/\, schema = \"APOLLO\"//g" $i
done


for i in $(find . -name '*.kts'); do
  echo "Replacing on $i"
  sed -i "s/2.2.6.RELEASE/2.3.0.RELEASE/g" $i
done

for i in $(find . -name '*.kt'); do
  echo "Replacing on $i"
  sed -i '/^class.*/i @Deprecated(\"Functionality Moved to apollo-ipc module\")' $i
done

gradle -D"CONFIG_PATH=D:\\kzmuhia\\Developers\\git\\apollo\\conf\\test" :apollo-api:bootRun --info
gradle  apollo-ipc:bootRun --info


gradle apollo-config:test --tests org.kebs.app.kotlin.apollo.config.security.HashingImplementationTest.hashString --info
gradle apollo-config:test --tests org.kebs.app.kotlin.apollo.config.service.adaptor.akka.actors.TestActorTest.testActors --info
gradle apollo-kafka-consumer-adaptor:test --tests org.kebs.app.kotlin.apollo.ipc.workflows.WorkFlowProcessServicesIntegrationTester.workFlowTest --info
gradle apollo-kafka-consumer-adaptor:test --tests org.kebs.app.kotlin.apollo.ipc.workflows.WorkFlowProcessServicesIntegrationTester.jsonToObjectUsingGson --info
gradle apollo-store:test --tests org.kebs.app.kotlin.apollo.store.StoreApplicationTest.contextLoads --info
gradle apollo-api:test --tests org.kebs.app.kotlin.apollo.api.ApiApplicationTest.submitUserRegistrationToKafka --info
gradle apollo-api:test --tests org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.BpmnTests.happyPathTest --info



Get-ChildItem -Recurse *.kt | Select-String -Pattern "Table" | Select-Object -Unique Path| sed -e 's/.*://g'


#gci D:\kzmuhia\Developers\git\apollo\apollo-store\src\main\kotlin\org\kebs\app\kotlin\apollo\store\model *.		kt -recurse | ForEach {  (Get-Content $_ | ForEach {$_ -replace ", schema = "QIAMSSADM"", ""}) | Set-Content $_}

git rm -r --cached *.idea/*

#### KEBS-KAFKA-01	KEBS-KAFKA-01.kebs.org	KEBS-KAFKA-02	KEBS-KAFKA-02.kebs.org	KEBS-KAFKA-03	KEBS-KAFKA-03.kebs.org

./kafka-run-class.sh kafka.tools.ConsumerOffsetChecker  --topic select-notifications-use-case --zookeeper KEBS-KAFKA-02:2181 --group apollo-sit

./kafka-topics.sh --describe --topic select-notifications-use-case --zookeeper KEBS-KAFKA-02:2181

./kafka-run-class.sh kafka.admin.ConsumerGroupCommand --group apollo-sit --bootstrap-server KEBS-KAFKA-02:9092 --describe
./kafka-run-class kafka.admin.ConsumerGroupCommand --group apollo-sit --bootstrap-server KEBS-KAFKA1-UAT:9092 --describe
./kafka-run-class.sh kafka.admin.ConsumerGroupCommand --group leto --bootstrap-server messaging_app1:9092 --describe

./kafka-topics.sh --describe --zookeeper localhost:2181 --topic request-reply-topic

./kafka-topics.sh --zookeeper localhost:2181 --delete --topic select-notifications-use-case

ssh -L 9092:10.9.0.9:9092 -L 1535:10.9.0.5:1535 kmuhia@40.120.24.168

Invoke-RestMethod -Uri "http://127.0.0.1:8006/login" -Method POST -Body @{user="vmuriuki";password="password"}

curl -vv --header "Content-Type: application/json" --request -X POST -d '{"loginRequest": {"username": "vmuriuki","password": "password"}}' http://127.0.0.1:8006/api/token/generate
curl -vv -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"username": "vmuriuki","password": "password"}' http://127.0.0.1:8006/api/token/generate

curl -vv --header "Content-Type: application/json" --header "Accept: application/json"  --request -X POST --data ' {"username": "vmuriuki","password": "password"}' http://127.0.0.1:8006/api/token/generate

curl -vv --header "Content-Type: application/json" --header "Accept: application/json" --data '{"username": "vmuriuki","password": "password"}' http://127.0.0.1:8006/api/token/generate
curl -X POST -H "Content-Type: application/json" -d '{"username":"vmuriuki","password":"password"} http://127.0.0.1:8005/api/integ/login

curl -H 'Accept: application/json' -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDc2MjYxMDgsImV4cCI6MTYwNzYyNjcwOCwicm9sZXMiOiJBVVRIT1JJVElFU19SRUFELEFVVEhPUklUSUVTX1dSSVRFLENEX1NVUEVSVklTT1JfREVMRVRFLENEX1NVUEVSVklTT1JfTU9ESUZZLENEX1NVUEVSVklTT1JfUkVBRCxNU19DT01QTEFJTlRfQUNDRVBULE1TX0hPRF9NT0RJRlksTVNfSE9EX1JFQUQsUFZPQ19BUFBMSUNBVElPTl9SRUFELFVTRVJfTUFOQUdFTUVOVF9ERUxFVEUsVVNFUl9NQU5BR0VNRU5UX01PRElGWSxVU0VSX01BTkFHRU1FTlRfUkVBRCJ9.s-Mn1N8vUVQfbLD7H7wpkHjLifiwa1vzGzhP25IKtBNwElqKa6VRQ0CfJgDgYY0kWruLz78r9TDMB26HncFBHw" http://127.0.0.1:8006/api/pvoc/send/coc



curl -X POST -H 'Accept: application/json' -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDc2OTc5MzYsImV4cCI6MTYwNzY5ODUzNiwicm9sZXMiOiJBVVRIT1JJVElFU19SRUFELEFVVEhPUklUSUVTX1dSSVRFLENEX1NVUEVSVklTT1JfREVMRVRFLENEX1NVUEVSVklTT1JfTU9ESUZZLENEX1NVUEVSVklTT1JfUkVBRCxNU19DT01QTEFJTlRfQUNDRVBULE1TX0hPRF9NT0RJRlksTVNfSE9EX1JFQUQsUFZPQ19BUFBMSUNBVElPTl9SRUFELFVTRVJfTUFOQUdFTUVOVF9ERUxFVEUsVVNFUl9NQU5BR0VNRU5UX01PRElGWSxVU0VSX01BTkFHRU1FTlRfUkVBRCJ9.SqHdMT8iqC2x43jA9v8JSwIZJl__RHG7Ap6R8igkqpQYbi57s8Ux6J0GT0JMFf5ssp975IqisejNr3unVpX9uA" -vv -H "Content-Type: application/json" -d '{"rfcNumber": "111","idfNumber": "111","ucrNumber": "111","rfcDate": "1111"}' http://127.0.0.1:8005/api/pvoc/send/coc

keytool -genkeypair -alias apollo -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore apollo-keystore.p12 -validity 3650 #apollo@123
keytool -importkeystore -srckeystore baeldung.jks -destkeystore baeldung.p12 -deststoretype pkcs12


keytool -import -alias bundle -trustcacerts -file __bskglobaltech_com.ca-bundle -keystore kappa-keystore.jks


ssh -L 9092:10.0.0.11:9092 -L 1535:10.0.0.32:1535 enyaga@193.34.145.118

sed -i 's/"[^"]*"/\U\0/g' filename #change characters within quotes to uppercase