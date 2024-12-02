package es.ndc.api_movies.Config;

import org.springframework.context.annotation.Configuration;

import es.ndc.api_movies.entities.GenreEntity;
import es.ndc.api_movies.repositories.GenreRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Configuration
public class GenreConfig {

    private final GenreRepository genreRepository;

    public GenreConfig(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @PostConstruct
    public void initGenres() {
        createGenreIfNotFound("Acción");
        createGenreIfNotFound("Comedia");
        createGenreIfNotFound("Drama");
        createGenreIfNotFound("Fantasía");
        createGenreIfNotFound("Terror");
        createGenreIfNotFound("Ciencia Ficción");
        createGenreIfNotFound("Romántico");
        createGenreIfNotFound("Suspense");
        createGenreIfNotFound("Aventura");
        createGenreIfNotFound("Animación");
    }

    @Transactional
    public void createGenreIfNotFound(String name) {
        GenreEntity genre = genreRepository.findByName(name);
        if (genre == null) {
            genre = new GenreEntity(name);
            genreRepository.save(genre);
        }
    }
}


