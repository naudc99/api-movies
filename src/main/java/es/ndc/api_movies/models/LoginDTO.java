package es.ndc.api_movies.models;

import lombok.Data;

@Data
public class LoginDTO {

    private String email;
    private String password;

}
