import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

public class WebscraperDora {
    private final String baseURL;

    public WebscraperDora() {
        this.baseURL = "https://www.imdb.com/";
    }

    // gets up to the first 20 keywords of a given movie and outputs movies with more than 2 shared keywords
    public String getKeywordRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> movieCounts = new HashMap<>();
        // URL for the IMDb page of the keywords for the given movie
        String keywordURL = baseURL + "title/" + movieID + "/keywords";
        String result = "";
        try {
            // accesses the IMDb page of the keywords for the given movie
            Document listKeywordsDoc = Jsoup.connect(keywordURL).get();
            Elements docDivs = listKeywordsDoc.select("div#keywords_content");
            for (Element div : docDivs) {
                // finds the table of keywords
                Elements keywords = div.selectFirst("table").select("td");
                Document keywordsDoc;
                int count = 0;
                for (Element keyword: keywords) {
                    // gets at most the first 20 keywords of the keywords list (to minimize run time)
                    if (count < 20) {
                        String urlExt = keyword.selectFirst("a").attr("href");
                        try {
                            urlExt = urlExt.split("&")[0];
                            // ensures that the top recommendations of a specific keyword are feature films
                            urlExt += "&ref_=kw_ref_typ&sort=moviemeter,asc&mode=detail&page=1&title_type=movie";
                            // accesses the page of top recommendations for a specific keyword
                            keywordsDoc = Jsoup.connect(baseURL + urlExt).get();
                            Elements keywordDocDivs = keywordsDoc.select("div.lister-item mode-detail");
                            for (Element keywordDocDiv : keywordDocDivs) {
                                String title = keywordDocDiv.selectFirst("h3").select("a").text();
                                // adds the movies to a HashMap while counting the times a movie appears
                                if (movieCounts.containsKey(title)) {
                                    movieCounts.put(title, movieCounts.get(title) + 1);
                                } else {
                                    movieCounts.put(title, 1);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("exception: URL - " + baseURL + urlExt);
                        }
                    }
                    count++;
                }
            }
        } catch(IOException e) {
            System.out.println("exception: URL - " + keywordURL);
        }
        for (String key : movieCounts.keySet()) {
            if (movieCounts.get(key) > 2 && !key.equals(movieTitle)) {
                result += key + " (" + movieCounts.get(key) + "), ";
            }
        }
        return result.substring(0, result.length() -2);
    }

    // gets the genres of a given movie and outputs movies with at least 2 shared genres of the top movies per genre
    public String getGenreRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> movieCounts = new HashMap<>();
        String movieURL = baseURL + "title/" + movieID;
        String result = "";
        try {
            // accesses the IMDb page for the given movie
            Document moviePage = Jsoup.connect(movieURL).get();
            Elements docDivs = moviePage.select("div.ipc-chip-list__scroller");
            for (Element div : docDivs) {
                // finds the movie genres
                Elements genres = div.select("a");
                Document genresDoc;
                for (Element genre: genres) {
                    String urlExt = genre.attr("href");
                    try {
                        urlExt = urlExt.split("&")[0];
                        // ensures that all genre recommendations are movies
                        urlExt += "&title_type=movie&ref_=adv_explore_rhs";
                        // accesses the page of top movies for each genre
                        genresDoc = Jsoup.connect(baseURL + urlExt).get();
                        Elements genreDocDivs = genresDoc.select("div.lister-item.mode-advanced");
                        for (Element genreDocDiv : genreDocDivs) {
                            String title = genreDocDiv.selectFirst("h3").select("a").text();
                            // adds the movies to a HashMap while counting the times a movie appears
                            if (movieCounts.containsKey(title)) {
                                movieCounts.put(title, movieCounts.get(title) + 1);
                            } else {
                                movieCounts.put(title, 1);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("exception: URL - " + baseURL + urlExt);
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("exception: URL - " + movieURL);
        }
        for (String key : movieCounts.keySet()) {
            if (movieCounts.get(key) > 1 && !key.equals(movieTitle)) {
                result += key + " (" + movieCounts.get(key) + "), ";
            }
        }
        return result.substring(0, result.length() -2);
    }

    // gets the average ratings of all movies directors of a given movie and outputs their highest rated movie
    public String getDirectorRecommendations(String movieID, String movieTitle) {
        HashMap<String, Integer> directorCounts = new HashMap<>();
        HashMap<String, Double> directorSums = new HashMap<>();
        String creditsURL = baseURL + "title/" + movieID + "/fullcredits";
        String result = "";
        try {
            // accesses the IMDb page for the full credits of the given movie
            Document moviePage = Jsoup.connect(creditsURL).get();
            Elements docDivs = moviePage.select("div#fullcredits_content");
            for (Element div : docDivs) {
                // finds the directors
                Elements directors = div.selectFirst("table").selectFirst("tbody").select("a");
                for (Element director: directors) {
                    String urlExt = director.attr("href");
                    String directorName = director.text();
                    try {
                        // accesses the page of each director
                        Document directorDoc = Jsoup.connect(baseURL + urlExt).get();
                        Elements directedList = directorDoc.select("div");
                        Document directedMovieDoc;
                        for (int i = 1; i < directedList.size(); i++) {
                            if (directedList.get(i - 1).className().equals("ipc-title ipc-title--base ipc-title--title " +
                                    "ipc-title--on-textPrimary sc-4390696d-4 hPNkDc filmo-section-director")) {
                                Elements previousMoviesList = directedList.get(i).select
                                        ("div.ipc-accordion__item__content_inner.accordion-content");
                                Elements previousMovies = previousMoviesList.get(previousMoviesList.size() - 1)
                                        .select("a");
                                String title;
                                for (Element movie : previousMovies) {
                                    urlExt = movie.attr("href");
                                    try {
                                        directorSums.put(directorName, 0.0);
                                        directorCounts.put(directorName, 0);
                                        directedMovieDoc = Jsoup.connect(baseURL + urlExt +
                                                "/ratings?ref_=tt_ql_4").get();
                                        System.out.println(directedMovieDoc.text());
                                        double trueRating = Double.parseDouble(directedMovieDoc
                                                .selectFirst(".ipl-rating-star__rating").text());
                                        // adds the movies to a HashMap while counting the times a movie appears
                                        directorCounts.put(directorName, directorCounts.get(directorName) + 1);
                                        directorSums.put(directorName, directorSums.get(directorName) + trueRating);
                                    } catch (IOException e) {
                                        System.out.println("exception: URL - " + baseURL + urlExt);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("exception: URL - " + baseURL + urlExt);
                    }
                }
            }
        } catch(IOException e){
            System.out.println("exception: URL - " + creditsURL);
        }
        System.out.println(directorCounts);
        for (String key : directorCounts.keySet()) {
            result += key + " (" + (directorSums.get(key) / directorCounts.get(key)) + "), ";
        }
        return result.substring(0, result.length() -2);
    }

    public String getDemographicRating(String movieID, String gender, int age) {
        String ratingsURL = baseURL + "title/" + movieID + "/ratings";
        try {
            // accesses the IMDb page of the ratings for a given movie
            Document ratingsPage = Jsoup.connect(ratingsURL).get();
            return ratingsPage.text();
        } catch (IOException e) {
            System.out.println("exception: URL - " + ratingsURL);
        }
        return "";
    }


    // gets the rating of a movie for a given age and gender and comparison to the average movie rating
    /*public String getDemographicRating(String movieID, String gender, int age) {
        String ratingsURL = baseURL + "title/" + movieID + "/ratings";
        try {
            // accesses the IMDb page of the ratings for a given movie
            Document ratingsPage = Jsoup.connect(ratingsURL).get();

            System.out.println(ratingsPage.text());
            double trueRating = Double.parseDouble(ratingsPage.selectFirst(".ipl-rating-star__rating").text());
            Elements ratingRows = ratingsPage.select("table").get(1).select("tr");
            // sets the row variable to match the given gender
            int genderRow;
            if (gender.equals("Male")) {
                genderRow = 2;
            } else if (gender.equals("Female")) {
                genderRow = 3;
            } else {
                // if the gender is not Male or Female it returns the average of both genders
                genderRow = 1;
            }
            // gets the cells in the row of the right gender
            Elements ratingCols = ratingRows.get(genderRow).select("td");
            int ageColumn;
            // sets the column variable to match the given age
            if (age < 18) {
                ageColumn = 2;
            } else if (age < 30) {
                ageColumn = 3;
            } else if (age < 45) {
                ageColumn = 4;
            } else {
                ageColumn = 5;
            }
            // get the correct cell using age ranges and the given age
            Element ratingCell = ratingCols.get(ageColumn).selectFirst("div");
            // get the demographic rating as a double
            double demographicRating = Double.parseDouble(ratingCell.text());
            // returns the demographic rating and IMDb rating
            return "Demographic Rating for a(n) " + age + " year-old " + gender + ": " + demographicRating
                    + " (IMDb rating: " + trueRating + ")";
        } catch(IOException e) {
            System.out.println("exception: URL - " + ratingsURL);
        }
        return "";
    }*/

}
