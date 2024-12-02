package es.ndc.api_movies.models;

import es.ndc.api_movies.entities.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private Long movieId;
    private String title;
    private String synopsis;
    private Integer year;
    private Integer duration;
    private byte[] poster;
    private String trailer;
    private Long directorId;
    private List<Long> actorIds;
    private List<Long> genreIds;
    private Double rating; // Nuevo campo para el rating promedio

    public MovieDTO(MovieEntity movieEntity) {
        this.movieId = movieEntity.getMovieId();
        this.title = movieEntity.getTitle();
        this.synopsis = movieEntity.getSynopsis();
        this.year = movieEntity.getYear();
        this.duration = movieEntity.getDuration();
        this.poster = movieEntity.getPoster();
        this.trailer = movieEntity.getTrailer();
        this.rating = movieEntity.getRating();  // Asignar el valor del rating

        // Mapeo de IDs de director, actores y géneros
        this.directorId = movieEntity.getDirector() != null ? movieEntity.getDirector().getDirectorId() : null;
        this.actorIds = movieEntity.getActors().stream().map(actor -> actor.getActorId()).collect(Collectors.toList());
        this.genreIds = movieEntity.getGenres().stream().map(genre -> genre.getGenreId()).collect(Collectors.toList());
    }
}
