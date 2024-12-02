package es.ndc.api_movies.services;

import es.ndc.api_movies.entities.DirectorEntity;
import es.ndc.api_movies.entities.GenreEntity;
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
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    // Obtener todos los géneros
    public List<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    // Obtener un género por ID
    public GenreEntity getGenreById(Long id) {
        return genreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genre not found with ID: " + id));
    }

    // Crear un nuevo género
    public GenreEntity createGenre(GenreEntity genre) {
        return genreRepository.save(genre);
    }

    // Actualizar un género existente
    public GenreEntity updateGenre(Long id, GenreEntity genreDetails) {
        GenreEntity genre = genreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genre not found with ID: " + id));

        genre.setName(genreDetails.getName());
        return genreRepository.save(genre);
    }

    // Eliminar un género
    public void deleteGenre(Long id) {
        GenreEntity genre = genreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genre not found with ID: " + id));

        genreRepository.delete(genre);
    }

    public Map<String, Object> getAllGenresPaginated(int page, int size, String sortBy, String name) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<GenreEntity> genresPage;

        if (name != null && !name.isEmpty()) {
            // Filtrar por el nombre de género (ignorando mayúsculas/minúsculas)
            genresPage = genreRepository.findByNameContainingIgnoreCase(name, paging);
        } else {
            // Si no hay nombre, devolver todos los géneros
            genresPage = genreRepository.findAll(paging);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", genresPage.getContent());
        response.put("totalElements", genresPage.getTotalElements());
        response.put("totalPages", genresPage.getTotalPages());
        response.put("currentPage", genresPage.getNumber());

        return response;
    }

}
