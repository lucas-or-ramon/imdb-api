package br.com.devcanoa.imdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JacksonJsonParser;

import javax.management.Attribute;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

@SpringBootApplication
public class ImdbApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImdbApiApplication.class, args);

        HttpClient client = HttpClient.newHttpClient();

        String apiKey = System.getenv("apiKey");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://imdb-api.com/en/API/Top250Movies/" + apiKey))
                .GET()
                .build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).join();

        List<Object> moviesList = getMoviesList(response);

        List<String> titles = getListAttributes("title", moviesList);
        List<String> urlImages = getListAttributes("image", moviesList);
        List<String> rating = getListAttributes("imDbRating", moviesList);
        List<String> years = getListAttributes("year", moviesList);

//        System.out.println(objects);
    }

    private static List<String> getListAttributes(String type, List<Object> moviesList) {
        List<String> strings = moviesList.stream().map(movie -> movie.toString().split(type+"=")[1].split(",")[0]).collect(Collectors.toList());
        return strings;
    }

    private static List<Object> getMoviesList(String response) {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> objects = jsonParser.parseMap(response);
        List<Object> moviesList = (ArrayList) objects.get("items");
        return moviesList;
    }
}
