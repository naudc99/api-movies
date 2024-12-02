package es.ndc.api_movies.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<MovieEntity> movies;

    public GenreEntity(String name) {
        this.name = name;
    }
}

