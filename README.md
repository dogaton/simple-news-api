### Run
To run application run `mvn spring-boot:run`

### Endpoints
When the application is running you can test the two endpoints on localhost:8080

Returns by default ten articles from gnews.
You can add a query param `count` for the number of articles returned

`/news/artictles`

Example: http://localhost:8080/news/articles?count=5

Requiered query param is `keyword`.
Optional query param is `title`

`/news/articles/search`

Example: http://localhost:8080/news/articles/search?keyword=spirit&title=JetBlue
