package es.ndc.api_movies.services;

import es.ndc.api_movies.entities.ActorEntity;
import es.ndc.api_movies.models.ActorDTO;
import es.ndc.api_movies.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    // Obtener todos los actores
    public List<ActorEntity> getAllActors() {
        return actorRepository.findAll();
    }

    // Obtener un actor por ID
    public ActorEntity getActorById(Long id) {
        return actorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));
    }

    // Crear un nuevo actor con nombre y foto de perfil
    public ActorEntity createActor(ActorDTO actorDTO) {
        ActorEntity actor = new ActorEntity();
        actor.setName(actorDTO.getName());
        actor.setProfile_photo(actorDTO.getProfile_photo());
        return actorRepository.save(actor);
    }

    // Actualizar un actor existente
    public ActorEntity updateActor(Long id, ActorDTO actorDTO) {
        ActorEntity actor = actorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));

        actor.setName(actorDTO.getName());
        actor.setProfile_photo(actorDTO.getProfile_photo());
        return actorRepository.save(actor);
    }

    // Eliminar un actor
    public void deleteActor(Long id) {
        ActorEntity actor = actorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));
        actorRepository.delete(actor);
    }

    public Map<String, Object> getAllActorsPaginated(int page, int size, String sortBy, String name) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ActorEntity> actorsPage = actorRepository.findAll(paging);

        if (name != null && !name.isEmpty()) {
            // Filtrar por el nombre de género (ignorando mayúsculas/minúsculas)
            actorsPage = actorRepository.findByNameContainingIgnoreCase(name, paging);
        } else {
            // Si no hay nombre, devolver todos los géneros
            actorsPage = actorRepository.findAll(paging);
        }

        // Crear un mapa con los datos necesarios para la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("content", actorsPage.getContent());
        response.put("totalElements", actorsPage.getTotalElements());
        response.put("totalPages", actorsPage.getTotalPages());
        response.put("currentPage", actorsPage.getNumber());

        return response;
    }
}

