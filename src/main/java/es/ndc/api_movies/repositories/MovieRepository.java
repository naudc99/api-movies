package es.ndc.api_movies.repositories;

import es.ndc.api_movies.entities.DirectorEntity;
import es.ndc.api_movies.entities.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Page<MovieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
