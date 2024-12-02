package es.ndc.api_movies.controllers;

import es.ndc.api_movies.entities.DirectorEntity;
import es.ndc.api_movies.models.DirectorDTO;
import es.ndc.api_movies.services.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/directors")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    // Obtener todos los directores

    // Obtener un director por ID
    @GetMapping("/{id}")
    public ResponseEntity<DirectorEntity> getDirectorById(@PathVariable Long id) {
        DirectorEntity director = directorService.getDirectorById(id);
        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    // Crear un nuevo director con nombre y foto de perfil
    @PostMapping
    public ResponseEntity<DirectorEntity> createDirector(@RequestBody DirectorDTO directorDTO) {
        DirectorEntity newDirector = directorService.createDirector(directorDTO);
        return new ResponseEntity<>(newDirector, HttpStatus.CREATED);
    }

    // Actualizar un director existente
    @PutMapping("/{id}")
    public ResponseEntity<DirectorEntity> updateDirector(
        @PathVariable Long id,
        @RequestBody DirectorDTO directorDTO
    ) {
        DirectorEntity updatedDirector = directorService.updateDirector(id, directorDTO);
        return new ResponseEntity<>(updatedDirector, HttpStatus.OK);
    }

    // Eliminar un director
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDirectorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String name) {

        Map<String, Object> response = directorService.getAllDirectorsPaginated(page, size, sortBy, name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
