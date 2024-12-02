package es.ndc.api_movies.controllers;

import es.ndc.api_movies.entities.MovieEntity;
import es.ndc.api_movies.models.MovieDTO;
import es.ndc.api_movies.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Obtener todas las películas

    // Obtener una película por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovieEntity> getMovieById(@PathVariable Long id) {
        MovieEntity movie = movieService.getMovieById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    // Crear una nueva película
    @PostMapping
    public ResponseEntity<MovieEntity> createMovie(@RequestBody MovieDTO movieDTO) {
        MovieEntity newMovie = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    // Actualizar una película existente
    @PutMapping("/{id}")
    public ResponseEntity<MovieEntity> updateMovie(
        @PathVariable Long id,
        @RequestBody MovieDTO movieDTO
    ) {
        MovieEntity updatedMovie = movieService.updateMovie(id, movieDTO);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    // Eliminar una película
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMoviesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String tittle) {

        Map<String, Object> response = movieService.getAllMoviesPaginated(page, size, sortBy, tittle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}