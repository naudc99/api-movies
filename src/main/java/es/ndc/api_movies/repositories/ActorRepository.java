package es.ndc.api_movies.repositories;

import es.ndc.api_movies.entities.ActorEntity;
import es.ndc.api_movies.entities.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<ActorEntity, Long> {

    Page<ActorEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

