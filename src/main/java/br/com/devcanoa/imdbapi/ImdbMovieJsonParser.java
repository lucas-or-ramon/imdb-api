package br.com.devcanoa.imdbapi;

import org.springframework.boot.json.JacksonJsonParser;

import java.util.ArrayList;
import java.util.List;

public class ImdbMovieJsonParser {
    private final String json;

    public ImdbMovieJsonParser(String json) {
        this.json = json;
    }

    public List<Movie> parser() {
        List<Object> objects = getMoviesObjectsFromJson();
        List<Movie> movieList = new ArrayList<>();

        objects.forEach(object -> movieList.add(new Movie(
                getAttribute("title", object),
                getAttribute("image", object),
                getAttribute("imDbRating", object),
                getAttribute("year", object))));

        return movieList;
    }

    private List<Object> getMoviesObjectsFromJson() {
        return List.of(new JacksonJsonParser().parseMap(json).get("items"));
    }

    private String getAttribute(String attribute, Object movie) {
        return movie.toString().split(attribute + "=")[1].split(",")[0];
    }
}
