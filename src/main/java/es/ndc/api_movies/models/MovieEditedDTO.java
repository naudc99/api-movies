package es.ndc.api_movies.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieEditedDTO {
    private Long movieId;
    private String title;
    private String synopsis;
    private Integer year;
    private Integer duration;
    private byte[] poster;
    private String trailer;
}
