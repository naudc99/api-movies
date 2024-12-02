package es.ndc.api_movies.controllers;

import es.ndc.api_movies.entities.ActorEntity;
import es.ndc.api_movies.models.ActorDTO;
import es.ndc.api_movies.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    // Obtener todos los actores

    // Obtener un actor por ID
    @GetMapping("/{id}")
    public ResponseEntity<ActorEntity> getActorById(@PathVariable Long id) {
        ActorEntity actor = actorService.getActorById(id);
        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    // Crear un nuevo actor con nombre y foto de perfil
    @PostMapping
    public ResponseEntity<ActorEntity> createActor(@RequestBody ActorDTO actorDTO) {
        ActorEntity newActor = actorService.createActor(actorDTO);
        return new ResponseEntity<>(newActor, HttpStatus.CREATED);
    }

    // Actualizar un actor existente
    @PutMapping("/{id}")
    public ResponseEntity<ActorEntity> updateActor(
        @PathVariable Long id,
        @RequestBody ActorDTO actorDTO
    ) {
        ActorEntity updatedActor = actorService.updateActor(id, actorDTO);
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    // Eliminar un actor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllActorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String name){

        Map<String, Object> response = actorService.getAllActorsPaginated(page, size, sortBy, name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
