package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class Movie {
    private String title;
    private String year;
    private String genre;

    public Movie(String title, String year, String genre) {
        this.title = title;
        this.year = year;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "Title : " + title + ", Year : " + year + ", Genre : " + genre;
    }
}

public class OmdbApiExample {
    public static void main(String[] args) {
        try {
            //Insert OMDB API KEY HERE
            String apiKey = "";
            String[] movieTitles = {"Inception", "Interstellar", "The Dark Knight"};
            List<Movie> movies = new ArrayList<>();

            for (String title : movieTitles) {
                String formattedTitle = title.replace(" ", "+"); // The + Is because of the OMDB Request needs it like that
                String urlStr = "https://www.omdbapi.com/?apikey=" + apiKey + "&t=" + formattedTitle;
                URI uri = URI.create(urlStr);
                URL url = uri.toURL(); // Transforming URI TO URL
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                con.disconnect();

                // Parsing JSON Response and Creating a Movie object
                String jsonResponse = content.toString();
                String movieTitle = extractJsonField(jsonResponse, "Title");
                String movieYear = extractJsonField(jsonResponse, "Year");
                String movieGenre = extractJsonField(jsonResponse, "Genre");

                movies.add(new Movie(movieTitle, movieYear, movieGenre));
            }

            // Print Movies
            System.out.println("Movies: ");
            for (Movie movie : movies) {
                System.out.println(movie);
            }

            // Filtered by Year
            String filterYear = "2010";
            System.out.println("\nMovies from the year " + filterYear + ":");
            for (Movie movie : movies) {
                if (movie.getYear().equals(filterYear)) {
                    System.out.println(movie);
                }
            }

            // Filtered by Genre
            String filterGenre = "Action";
            System.out.println("\nMovies of the genre " + filterGenre + ":");
            for (Movie movie : movies) {
                if (movie.getGenre().contains(filterGenre)) {
                    System.out.println(movie);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractJsonField(String jsonResponse, String fieldName) {
        String searchStr = "\"" + fieldName + "\":\"";
        int startIndex = jsonResponse.indexOf(searchStr);
        if (startIndex == -1) {
            return "N/A"; // Not Found Response
        }
        startIndex += searchStr.length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        return jsonResponse.substring(startIndex, endIndex);
    }
}
