some useful comands
start cluster locally with port forward or host network
docker run -d --name=landoop -p 127.0.0.1:8081-8083:8081-8083 -p 127.0.0.1:9581-9585:9581-9585 -p 127.0.0.1:9092:9092 -p 127.0.0.1:3030:3030 -p 127.0.0.1:2181:2181 -e ADV_HOST=127.0.0.1 landoop/fast-data-dev
docker run --rm -it --net=host landoop/fast-data-dev bash

topics
kafka-topics --zookeeper localhost:2181 --list
kafka-topics --zookeeper localhost:2181 --describe --topic my-replicated-topic
kafka-topics --zookeeper localhost:2181 --describe --under-replicated-partitions
kafka-topics --zookeeper localhost:2181 --create --if-not-exists --replication-factor 1 --partitions 1 --topic test
kafka-topics --zookeeper localhost:2181 --create --replication-factor 3 --partitions 3 --topic my-replicated-topic
kafka-topics --zookeeper localhost:2181 --delete --topic second_topic

consumer-groups
kafka-consumer-groups --zookeeper localhost:2181 --list
kafka-consumer-groups --bootstrap-server localhost:9092 --list
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group tokencleanup-123234s

producer
kafka-console-producer --broker-list localhost:9092 --topic first_topic

consumer
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --from-beginning --topic first_topic (of no offset)
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --offset 0 --partition 0 --topic first_topic
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --consumer-property group.id=mygroup1 --topic first_topic
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --from-beginning --property print.key=true --topic firstTopic
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic __consumer_offsets --formatter 'kafka.coordinator.GroupMetadataManager$OffsetsMessageFormatter' --max-messages 1

configs
kafka-configs --zookeeper localhost:2181 --entity-type topics --describe --entity-name my-topic
kafka-configs --zookeeper localhost:2181 --entity-type topics --alter --entity-name my-topic --add-config retention.ms=360000
kafka-configs --zookeeper localhost:2181 --entity-type topics --alter --entity-name my-topic --delete-config retention.ms

other
kafka-preferred-replica-election --zookeeper localhost:2181
kafka-run-class kafka.tools.DumpLogSegments --files 00000000000052368601.log
kafka-run-class kafka.tools.DumpLogSegments --files 00000000000052368601.log --print-data-log
kafka-run-class kafka.tools.DumpLogSegments --files 00000000000052368601.index,00000000000052368601.log --index-sanity-check
kafka-replica-verification --broker-list localhost:9092 --topic-white-list 'my-.*'