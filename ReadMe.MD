
Java 17
Docker
Kubernetes
Spring Rest
Swagger -   Open API
JUnit, Mockitho, Integration Testing
Kafka, Kafka Streams
MongoDB
Postgres
GraphQl
Event Driven Micro Services
Azure
AWS

****************************************************************************************

index.csv:

NYA,1965-12-31,528.690002,528.690002,528.690002,28.690002,68776.690002,0
NYAA,1966-01-06,532.070007,532.070007,532.070007,532.070007,532.070007,0
KA,1966-01-03,68776.210022,68776.210022,68776.210022,68776.210022,68776.210022,0
NYA,1966-01-04,527.840027,527.840027,527.840027,527.840027,527.840027,0
NYA,1966-01-05,531.119995,531.119995,531.119995,531.119995,531.119995,0
NYA,1966-01-07,532.599976,532.599976,532.599976,532.599976,532.599976,0
NYA,1966-01-10,533.869995,533.869995,533.869995,533.869995,533.869995,0
NYA,1966-01-11,534.289978,534.289978,534.289978,534.289978,534.289978,0
NYA,1966-01-12,533.340027,533.340027,533.340027,533.340027,533.340027,0
NYA,1966-01-13,534.400024,534.400024,534.400024,534.400024,534.400024,0


Producer, Stream Process & consumer apps also working...
Control Center is working, look at @ http://localhost:9021/clusters/
schema-registry:        http://localhost:8081/      #Up and working
connect:                http://localhost:8083/      #Up and working      
ksqldb-server:          http://localhost:8088/      #Up and working
rest-proxy:             http://localhost:8082/      #Up and working
Confluent CLI:      
Command line interface for administering your streaming service, including Apache Kafka topics, clusters, schemas, Connectors, ksqlDB, security, billing, and more.


Final output @ consumer ....
2023-12-24 22:17:21.553  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : ﻿NYA = 2
2023-12-24 22:17:21.553  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : KA = 2
2023-12-24 22:17:21.553  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : NYA = 27886
2023-12-24 22:17:21.553  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : IXIC = 25380
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : HSI = 17494
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : 000001.SS = 11926
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : GSPTSE = 21552
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : 399001.SZ = 11856
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : NSEI = 6762
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : GDAXI = 17212
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : KS11 = 12362
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : SSMI = 15660
2023-12-24 22:17:21.554  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : TWII = 12020
2023-12-24 22:17:21.555  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : J203.JO = 4774
2023-12-24 22:17:21.555  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : N225 = 29000
2023-12-24 22:17:21.555  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : N100 = 11014
2023-12-24 22:17:21.555  INFO 17363 --- [nio-9071-exec-1] c.t.c.analytics.KafkaConsumerController  : NYAA = 10


Avro schema registry:

https://docs.confluent.io/platform/current/schema-registry/index.html