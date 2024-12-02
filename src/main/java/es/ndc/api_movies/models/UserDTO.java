package es.ndc.api_movies.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import es.ndc.api_movies.entities.UserEntity;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String name;
    private String email;
    private byte[] image;
    private List<MovieDTO> favoriteMovies;  // Agrega la lista de películas favoritas
    private List<MovieDTO> watchedMovies;   // Agrega la lista de películas vistas

    public UserDTO(UserEntity user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.image = user.getImage();
        
        this.favoriteMovies = user.getFavoriteMovies()
                                  .stream()
                                  .map(movie -> new MovieDTO(movie))  
                                  .collect(Collectors.toList());
        
        this.watchedMovies = user.getWatchedMovies()
                                 .stream()
                                 .map(movie -> new MovieDTO(movie))  
                                 .collect(Collectors.toList());
    }
    
}
