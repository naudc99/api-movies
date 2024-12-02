package es.ndc.api_movies.services;

import es.ndc.api_movies.entities.*;
import es.ndc.api_movies.models.MovieDTO;
import es.ndc.api_movies.repositories.MovieRepository;
import es.ndc.api_movies.repositories.ActorRepository;
import es.ndc.api_movies.repositories.DirectorRepository;
import es.ndc.api_movies.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private GenreRepository genreRepository;

    // Obtener todas las películas
    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    // Obtener una película por ID
    public MovieEntity getMovieById(Long id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
    }

    // Crear una nueva película
    public MovieEntity createMovie(MovieDTO movieDTO) {
        
        MovieEntity movie = new MovieEntity();
        movie.setTitle(movieDTO.getTitle());
        movie.setSynopsis(movieDTO.getSynopsis());
        movie.setYear(movieDTO.getYear());
        movie.setDuration(movieDTO.getDuration());
        movie.setPoster(movieDTO.getPoster());
        movie.setTrailer(movieDTO.getTrailer());

        // Set director
        DirectorEntity director = directorRepository.findById(movieDTO.getDirectorId())
            .orElseThrow(() -> new RuntimeException("Director not found with ID: " + movieDTO.getDirectorId()));
        movie.setDirector(director);

        // Set actors
        List<ActorEntity> actors = actorRepository.findAllById(movieDTO.getActorIds());
        movie.setActors(actors);

        // Set genres
        List<GenreEntity> genres = genreRepository.findAllById(movieDTO.getGenreIds());
        movie.setGenres(genres);

        return movieRepository.save(movie);
    }

    // Actualizar una película existente
    public MovieEntity updateMovie(Long id, MovieDTO movieDTO) {
        MovieEntity movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));

        movie.setTitle(movieDTO.getTitle());
        movie.setSynopsis(movieDTO.getSynopsis());
        movie.setYear(movieDTO.getYear());
        movie.setDuration(movieDTO.getDuration());
        movie.setPoster(movieDTO.getPoster());
        movie.setTrailer(movieDTO.getTrailer());

        return movieRepository.save(movie);
    }

    // Eliminar una película
    public void deleteMovie(Long id) {
        MovieEntity movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
        movieRepository.delete(movie);
    }

    public Map<String, Object> getAllMoviesPaginated(int page, int size, String sortBy, String tittle) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<MovieEntity> moviesPage = movieRepository.findAll(paging);

        if (tittle != null && !tittle.isEmpty()) {
            // Filtrar por el nombre de género (ignorando mayúsculas/minúsculas)
            moviesPage = movieRepository.findByTitleContainingIgnoreCase(tittle, paging);
        } else {
            // Si no hay nombre, devolver todos los géneros
            moviesPage = movieRepository.findAll(paging);
        }

        // Crear un mapa con los datos necesarios para la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("content", moviesPage.getContent());
        response.put("totalElements", moviesPage.getTotalElements());
        response.put("totalPages", moviesPage.getTotalPages());
        response.put("currentPage", moviesPage.getNumber());

        return response;
    }

    public void updateMovieRating(MovieEntity movie, List<ReviewEntity> reviews) {
        if (reviews.isEmpty()) {
            movie.setRating(0.0);
        } else {
            double averageRating = reviews.stream()
                    .mapToDouble(ReviewEntity::getScore)
                    .average()
                    .orElse(0.0);
            movie.setRating(averageRating);
        }
        movieRepository.save(movie);
    }
}