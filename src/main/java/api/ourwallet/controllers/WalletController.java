package api.ourwallet.controllers;


import api.ourwallet.domains.User;
import api.ourwallet.domains.Wallet;
import api.ourwallet.dtos.WalletRequestDTO;
import api.ourwallet.repositories.UserRepository;
import api.ourwallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    private ResponseEntity<Wallet> createWallet(@RequestBody WalletRequestDTO body) {
        try{
            User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
            Wallet wallet = new Wallet();
            wallet.setBalance(0.0);
            Wallet newWallet = this.walletRepository.save(wallet);

            user.addWallet(newWallet);

            this.userRepository.save(user);

            return ResponseEntity.ok(newWallet);
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }



}
