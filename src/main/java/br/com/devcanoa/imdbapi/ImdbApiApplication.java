package br.com.devcanoa.imdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class ImdbApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImdbApiApplication.class, args);

        HttpClient client = HttpClient.newHttpClient();

        String apiKey = System.getenv("apiKey");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://imdb-api.com/en/API/Search/" + apiKey + "/inception%202010"))
                .GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }
}
