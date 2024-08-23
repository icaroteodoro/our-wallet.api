package api.ourwallet.controllers;


import api.ourwallet.domains.Wallet;
import api.ourwallet.infra.secutiry.TokenService;
import api.ourwallet.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor //Poderia substituir essa anotação pela anotação @Autowired em cima de cada dependencia
public class AuthController {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(user.getPassword(), body.password())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = this.userRepository.findByEmail(body.email());
        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));

            Wallet wallet = new Wallet();
            wallet.setBalance(0.0);
            Wallet newWallet = this.walletRepository.save(new Wallet());

            newUser.addWallet(newWallet);
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new RegisterResponseDTO( newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
