package es.ndc.api_movies.services;

import es.ndc.api_movies.entities.ActorEntity;
import es.ndc.api_movies.entities.DirectorEntity;
import es.ndc.api_movies.models.DirectorDTO;
import es.ndc.api_movies.repositories.DirectorRepository;
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
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    // Obtener todos los directores
    public List<DirectorEntity> getAllDirectors() {
        return directorRepository.findAll();
    }

    // Obtener un director por ID
    public DirectorEntity getDirectorById(Long id) {
        return directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found with ID: " + id));
    }

    // Crear un nuevo director con nombre y foto de perfil
    public DirectorEntity createDirector(DirectorDTO directorDTO) {
        DirectorEntity director = new DirectorEntity();
        director.setName(directorDTO.getName());
        director.setProfile_photo(directorDTO.getProfile_photo());
        return directorRepository.save(director);
    }

    // Actualizar un director existente
    public DirectorEntity updateDirector(Long id, DirectorDTO directorDTO) {
        DirectorEntity director = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found with ID: " + id));

        director.setName(directorDTO.getName());
        director.setProfile_photo(directorDTO.getProfile_photo());
        return directorRepository.save(director);
    }

    // Eliminar un director
    public void deleteDirector(Long id) {
        DirectorEntity director = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found with ID: " + id));
        directorRepository.delete(director);
    }

    public Map<String, Object> getAllDirectorsPaginated(int page, int size, String sortBy, String name) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<DirectorEntity> directorsPage = directorRepository.findAll(paging);

        if (name != null && !name.isEmpty()) {
            // Filtrar por el nombre de género (ignorando mayúsculas/minúsculas)
            directorsPage = directorRepository.findByNameContainingIgnoreCase(name, paging);
        } else {
            // Si no hay nombre, devolver todos los géneros
            directorsPage = directorRepository.findAll(paging);
        }

        // Crear un mapa con los datos necesarios para la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("content", directorsPage.getContent());
        response.put("totalElements", directorsPage.getTotalElements());
        response.put("totalPages", directorsPage.getTotalPages());
        response.put("currentPage", directorsPage.getNumber());

        return response;
    }
}
