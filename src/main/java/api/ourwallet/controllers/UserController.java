package api.ourwallet.controllers;


import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.User;
import api.ourwallet.domains.Wallet;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.ourwallet.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        try {
            User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            for(Wallet wallet : user.getWallets()) {
                this.walletRepository.deleteById(wallet.getId());
                this.transactionRepository.deleteTransactionsByWallet(wallet);
            }
            this.userRepository.deleteById(id);

            return ResponseEntity.ok().build();
        }catch(Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.badRequest().build();
        }
    }


}
