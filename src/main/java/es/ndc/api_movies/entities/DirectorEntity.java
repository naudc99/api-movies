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
@Table(name = "directors")
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long directorId;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "profile_photo", length = 10485760)
    private byte[] profile_photo;

    @JsonIgnore
    @OneToMany(mappedBy = "director")
    private List<MovieEntity> movies;
}

