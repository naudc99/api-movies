package es.ndc.api_movies.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDTO {
    private Long directorId;
    private String name;
    private byte[] profile_photo; 
}
