package es.ndc.api_movies.repositories;

import es.ndc.api_movies.entities.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    GenreEntity findByName(String name);
    Page<GenreEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

