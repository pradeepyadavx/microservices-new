# microservices-new
#Product Service :8080 Mongo db
#Order Service :8081 mysql
#Inventory Service :8082 mysql

eureka server
can be accessed at
http://localhost:8080/eureka/web



api gateway is conf at 8080




api gateway is secured by oauth2 using keycloak
refer video 5


## Run keycloak

docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:18.0.0 start-dev

either import  realm-export.json 
or follow below steps

make new  realm ->  spring-boot-microservices-realm
make client -> spring-cloud-client
choose access type-> confidential
disable -> Standard web flow and Direct access grant enabled
enable -> Service Accounts Enabled 
credentials -> get from client

## Run Zipkin
docker run -d -p 9411:9411 openzipkin/zipkin

## Run kafka on docker 
1. open terminal  in root folder
2.  docker compose up -d