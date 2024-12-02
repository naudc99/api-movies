package es.ndc.api_movies.services;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import es.ndc.api_movies.models.PasswordUpdateDTO;
import es.ndc.api_movies.models.ResponseDTO;
import es.ndc.api_movies.models.UserDTO;
import es.ndc.api_movies.models.UserRolesDTO;
import es.ndc.api_movies.repositories.MovieRepository;
import es.ndc.api_movies.repositories.UserRepository;
import es.ndc.api_movies.entities.MovieEntity;
import es.ndc.api_movies.entities.UserEntity;

import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public UserService(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository=movieRepository;
    }

    public ResponseEntity<UserDTO> getUserById(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserRolesDTO> getUserRoles(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(user.toRolesDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<UserRolesDTO>> getAllUsers() {
        try {
            if (!isADMIN())
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            List<UserEntity> users = userRepository.findAll();
            List<UserRolesDTO> userDTOS = new ArrayList<>();
            if (users.isEmpty())
                return ResponseEntity.noContent().build();
            for (UserEntity userEntity : users)
                userDTOS.add(userEntity.toRolesDTO());
            return ResponseEntity.ok(userDTOS);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<ResponseDTO> updateUser(Long id, UserEntity updatedUser) {
        ResponseDTO response = new ResponseDTO();
        try {
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    
            // Solo actualiza el nombre de usuario, correo electrónico e imagen
            previousUser.setName(updatedUser.getName());
            previousUser.setEmail(updatedUser.getEmail());
            previousUser.setImage(updatedUser.getImage()); // Ajusta el método setImage según la lógica de tu aplicación
    
            userRepository.save(previousUser);
            response.newMessage("Usuario actualizado");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.newError("Id no encontrada");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            response.newError(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }


    public ResponseEntity<UserDTO> updateName(Long id, String nameNew) {
        try {
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setName(nameNew);
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserDTO> updateEmail(Long id, String emailNew) {
        try {
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            previousUser.setEmail(emailNew);
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserDTO> updatePassword(Long id, PasswordUpdateDTO passwords) {
        try {
            UserEntity previousUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            if (!passwords.getPasswordNew()
                    .matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#ñÑ])[A-Za-z\\d@$!%*?&#ñÑ]{8,}$")
                    || !verifyPassword(passwords.getPasswordOld(), previousUser.getPassword()))
                return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_ACCEPTABLE);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            previousUser.setPassword(encoder.encode(passwords.getPasswordNew()));
            final UserEntity user = userRepository.save(previousUser);
            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }

    public ResponseEntity<ResponseDTO> removeUser(Long userId) {
        ResponseDTO response = new ResponseDTO();
        try {
            if (!isADMIN())
                return new ResponseEntity<>(new ResponseDTO("No tienes permiso"), HttpStatus.UNAUTHORIZED);
            userRepository.deleteById(userId);
            response.newMessage("Usuario borrado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.newError(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    public boolean isADMIN() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities)
                if ("ADMIN".equals(authority.getAuthority()))
                    return true;
        }
        return false;
    }

    public ResponseEntity<UserDTO> addMovieToFavorites(Long userId, Long movieId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            MovieEntity movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));

            if (!user.getFavoriteMovies().contains(movie)) {
                user.getFavoriteMovies().add(movie);
                userRepository.save(user);
            }

            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserDTO> removeMovieFromFavorites(Long userId, Long movieId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            MovieEntity movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));

            user.getFavoriteMovies().remove(movie);
            userRepository.save(user);

            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserDTO> addMovieToWatched(Long userId, Long movieId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            MovieEntity movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));

            if (!user.getWatchedMovies().contains(movie)) {
                user.getWatchedMovies().add(movie);
                userRepository.save(user);
            }

            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<UserDTO> removeMovieFromWatched(Long userId, Long movieId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            MovieEntity movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));

            user.getWatchedMovies().remove(movie);
            userRepository.save(user);

            return ResponseEntity.ok(user.toDTO());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Método para obtener las películas favoritas del usuario
    public ResponseEntity<List<MovieEntity>> getFavoriteMovies(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            List<MovieEntity> favoriteMovies = user.getFavoriteMovies();
            return ResponseEntity.ok(favoriteMovies);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Método para obtener las películas vistas del usuario
    public ResponseEntity<List<MovieEntity>> getWatchedMovies(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            List<MovieEntity> watchedMovies = user.getWatchedMovies();
            return ResponseEntity.ok(watchedMovies);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}