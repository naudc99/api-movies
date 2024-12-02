package es.ndc.api_movies.controllers;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.ndc.api_movies.entities.UserEntity;
import es.ndc.api_movies.models.JwtTokenDTO;
import es.ndc.api_movies.models.LoginDTO;
import es.ndc.api_movies.models.ResponseDTO;
import es.ndc.api_movies.services.AuthService;
import es.ndc.api_movies.services.UserService;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController{

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllUserNames() {
        return authService.getAllUserNames();
    }

    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserEntity userNew, BindingResult result) throws Exception {
        return authService.register(userNew, result);
    }

    @PostMapping("/login")
    private ResponseEntity<JwtTokenDTO> login(@RequestBody LoginDTO loginRequest) throws Exception {
        return authService.login(loginRequest);
    }
}
