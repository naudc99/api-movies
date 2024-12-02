package es.ndc.api_movies.models;

import lombok.Data;


@Data
public class PasswordUpdateDTO {

    private String passwordNew;
    private String passwordOld;
}
