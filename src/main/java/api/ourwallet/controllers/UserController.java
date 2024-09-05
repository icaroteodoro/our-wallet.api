package api.ourwallet.controllers;


import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.User;
import api.ourwallet.domains.Wallet;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.WalletRepository;
import api.ourwallet.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.ourwallet.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;



    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = this.userService.findAllUUser();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        try {
            User user = this.userService.findUserById(id);
            this.userService.deleteUserByEmail(user.getEmail());
            return ResponseEntity.ok().body("User deleted successfully");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
