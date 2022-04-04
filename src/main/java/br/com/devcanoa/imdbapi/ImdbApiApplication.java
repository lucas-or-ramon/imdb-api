package br.com.devcanoa.imdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@SpringBootApplication
public class ImdbApiApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ImdbApiApplication.class, args);

        String apiKey = System.getenv("apiKey");

        String json = new ImdbApiClient(apiKey).getBody();

        List<Movie> movies = new ImdbMovieJsonParser(json).parser();

        PrintWriter printWriter = new PrintWriter("source.html");
        new HTMLGenerator(printWriter).generate(movies);
        printWriter.close();
    }
}
