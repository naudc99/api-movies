package es.ndc.api_movies.controllers;

import es.ndc.api_movies.models.ReviewDTO;
import es.ndc.api_movies.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Crear una nueva review
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    // Eliminar una review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getUserReviews(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Map<String, Object>> getReviewsByMoviePaginated(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "reviewId") String sortBy) {

        Map<String, Object> response = reviewService.getReviewsByMoviePaginated(movieId, page, size, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Editar una review
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> editReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.editReview(id, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

}


