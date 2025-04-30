# GraphQL Client Demo

Ця аплікація демонструє використання **Spring for GraphQL** з боку **клієнта**.
Після старту (`./gradlew bootRun`) у консолі зʼявляться:


brew install --cask temurin21


export JAVA_HOME=$(/usr/libexec/java_home -v 21)
java -version                          # має показати 21.0.7
cd ~/Desktop/keycloak-24.0.5
./bin/kc.sh start-dev --http-port=8080

./gradlew clean bootRun
./gradlew bootRun

./gradlew test

token http://localhost:8080/realms/library/protocol/openid-connect/token

http://localhost:8081/graphql

read viewer { "query": "{ books { data { id title publishedDate } } }", "variables": {} }


update editor only
{
"query": "mutation($inp:AddBookInput!){ addBook(input:$inp){ success message data { id title } } }",
"variables": {
"inp": {
"title": "Test2",
"publishedDate": "2025-05-01",
"authorId": 2
}
}
}
