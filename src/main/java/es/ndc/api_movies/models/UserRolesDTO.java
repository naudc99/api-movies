package es.ndc.api_movies.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import es.ndc.api_movies.entities.RoleEntity;
import es.ndc.api_movies.entities.UserEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDTO {

    private Long userId;
    private String name;
    private String email;
    private RoleEntity role;

    public UserRolesDTO(UserEntity user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}

