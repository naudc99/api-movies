package es.ndc.api_movies.repositories;

import es.ndc.api_movies.entities.MovieEntity;
import es.ndc.api_movies.entities.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByMovie(MovieEntity movie);
    List<ReviewEntity> findByUserId(Long userId);

    Page<ReviewEntity> findByMovie_MovieId(Long movieId, Pageable pageable);

}
