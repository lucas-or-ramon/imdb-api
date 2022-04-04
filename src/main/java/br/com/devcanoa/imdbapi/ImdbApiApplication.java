package br.com.devcanoa.imdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JacksonJsonParser;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;

@SpringBootApplication
public class ImdbApiApplication {
    public static void main(String[] args) throws IOException {
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

        List<Movie> movies = getListMoviesObjects(moviesList);

        PrintWriter printWriter = new PrintWriter("source.html");
        new HTMLGenerator(printWriter).generate(movies);
        printWriter.close();
    }

    private static List<Object> getMoviesList(String response) {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> objects = jsonParser.parseMap(response);
        return (ArrayList) objects.get("items");
    }

    private static List<Movie> getListMoviesObjects(List<Object> moviesList) {
        List<Movie> movies = new ArrayList<>();
        moviesList.forEach(movie -> movies.add(new Movie(
                getAttribute("title", movie),
                getAttribute("image", movie),
                getAttribute("imDbRating", movie),
                getAttribute("year", movie))));

        return movies;
    }

    private static String getAttribute(String type, Object movie) {
        return movie.toString().split(type + "=")[1].split(",")[0];
    }
}
