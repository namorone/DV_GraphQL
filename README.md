# GraphQL Client Demo

Ця аплікація демонструє використання **Spring for GraphQL** з боку **клієнта**.
Після старту (`./gradlew bootRun`) у консолі зʼявляться:
* список перших 5 країн Європи;
* окремий рядок з назвою столиці України.

Реальні запити виконуються до публічного API [https://countries.trevorblades.com](https://countries.trevorblades.com).

./gradlew clean bootRun
./gradlew test -  запуск тесту

http://localhost:8080/api/country/UA

http://localhost:8080/api/continent/EU?limit=3

http://localhost:8080/api/continent/AS
