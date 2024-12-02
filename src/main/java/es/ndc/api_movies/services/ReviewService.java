package es.ndc.api_movies.services;

import es.ndc.api_movies.entities.MovieEntity;
import es.ndc.api_movies.entities.ReviewEntity;
import es.ndc.api_movies.entities.UserEntity;
import es.ndc.api_movies.models.ReviewDTO;
import es.ndc.api_movies.repositories.MovieRepository;
import es.ndc.api_movies.repositories.ReviewRepository;
import es.ndc.api_movies.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    // Crear una nueva review
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // Crear una nueva entidad de Review
        ReviewEntity review = new ReviewEntity();
        review.setComment(reviewDTO.getComment());
        review.setScore(reviewDTO.getScore());

        // Obtener la película asociada
        MovieEntity movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + reviewDTO.getMovieId()));
        review.setMovie(movie);

        // Obtener el usuario asociado
        UserEntity user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + reviewDTO.getUserId()));
        review.setUser(user); // Asignar el usuario a la review

        // Guardar la review
        reviewRepository.save(review);

        // Recalcular el rating de la película
        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie);
        movieService.updateMovieRating(movie, reviews);

        // Convertir la entidad Review en DTO para devolver la respuesta
        return new ReviewDTO(
                review.getReviewId(),
                review.getComment(),
                review.getScore(),
                review.getMovie().getMovieId(),
                review.getUser().getId() // Ahora podemos obtener el userId
        );
    }

    // Eliminar una review
    public void deleteReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        MovieEntity movie = review.getMovie();
        reviewRepository.delete(review);

        // Recalcular el rating de la película
        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie);
        movieService.updateMovieRating(movie, reviews);
    }

    public List<ReviewDTO> getUserReviews(Long userId) {
        // Obtiene todas las reseñas y filtra las del usuario específico
        return reviewRepository.findAll().stream()
                .filter(review -> review.getUser().getId().equals(userId)) // Filtra por ID de usuario
                .map(this::convertToDTO) // Convierte a ReviewDTO
                .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(ReviewEntity review) {
        return new ReviewDTO(
                review.getReviewId(),
                review.getComment(),
                review.getScore(),
                review.getMovie().getMovieId(),
                review.getUser().getId()
        );
    }

    public Map<String, Object> getReviewsByMoviePaginated(Long movieId, int page, int size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ReviewEntity> reviewsPage = reviewRepository.findByMovie_MovieId(movieId, paging);

        // Convertir entidades a DTOs (opcional)
        List<ReviewDTO> reviewsDTO = reviewsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Crear un mapa con los datos necesarios para la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("content", reviewsDTO);
        response.put("totalElements", reviewsPage.getTotalElements());
        response.put("totalPages", reviewsPage.getTotalPages());
        response.put("currentPage", reviewsPage.getNumber());

        return response;
    }

    // Editar una review
    // Editar una review
    public ReviewDTO editReview(Long reviewId, ReviewDTO reviewDTO) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        // Actualiza los campos necesarios
        review.setComment(reviewDTO.getComment());
        review.setScore(reviewDTO.getScore());

        // Guarda la reseña actualizada
        reviewRepository.save(review);

        // Recalcular el rating de la película
        MovieEntity movie = review.getMovie(); // Obtiene la película asociada
        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie); // Obtiene todas las reseñas de la película
        movieService.updateMovieRating(movie, reviews); // Actualiza la calificación de la película

        // Devuelve el DTO actualizado
        return convertToDTO(review);
    }
}


