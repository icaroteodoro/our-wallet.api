package api.ourwallet.controllers;


import api.ourwallet.infra.security.TokenService;
import api.ourwallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import api.ourwallet.domains.User;
import api.ourwallet.dtos.LoginRequestDTO;
import api.ourwallet.dtos.LoginResponseDTO;
import api.ourwallet.dtos.RegisterRequestDTO;
import api.ourwallet.dtos.RegisterResponseDTO;
import api.ourwallet.repositories.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor //Poderia substituir essa anotação pela anotação @Autowired em cima de cada dependencia
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        try{
            User newUser = this.userService.createUser(new User(body));
            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new RegisterResponseDTO( newUser.getName(), token));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
