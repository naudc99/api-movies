package es.ndc.api_movies.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    private Long actorId;
    private String name;
    private byte[] profile_photo; 
}

