package es.ndc.api_movies.controllers;

import es.ndc.api_movies.entities.GenreEntity;
import es.ndc.api_movies.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // Obtener un género por ID
    @GetMapping("/{id}")
    public ResponseEntity<GenreEntity> getGenreById(@PathVariable Long id) {
        GenreEntity genre = genreService.getGenreById(id);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    // Crear un nuevo género
    @PostMapping
    public ResponseEntity<GenreEntity> createGenre(@RequestBody GenreEntity genre) {
        GenreEntity newGenre = genreService.createGenre(genre);
        return new ResponseEntity<>(newGenre, HttpStatus.CREATED);
    }

    // Actualizar un género
    @PutMapping("/{id}")
    public ResponseEntity<GenreEntity> updateGenre(@PathVariable Long id, @RequestBody GenreEntity genreDetails) {
        GenreEntity updatedGenre = genreService.updateGenre(id, genreDetails);
        return new ResponseEntity<>(updatedGenre, HttpStatus.OK);
    }

    // Eliminar un género
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllGenresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String name) { // Nuevo parámetro opcional 'name'

        Map<String, Object> response = genreService.getAllGenresPaginated(page, size, sortBy, name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

