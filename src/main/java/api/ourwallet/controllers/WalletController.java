package api.ourwallet.controllers;


import api.ourwallet.domains.Wallet;
import api.ourwallet.dtos.CreateWalletRequestDTO;
import api.ourwallet.dtos.DeleteWalletRequestDTO;
import api.ourwallet.repositories.UserRepository;
import api.ourwallet.repositories.WalletRepository;
import api.ourwallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    private ResponseEntity createWallet(@RequestBody CreateWalletRequestDTO body) {
        try{
            Wallet newWallet = this.walletService.createWallet(body.email());
            return ResponseEntity.ok(newWallet);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    private ResponseEntity deleteWallet(@RequestBody DeleteWalletRequestDTO body) {
        try{
            this.walletService.deleteWallet(body.walletId());
            return ResponseEntity.ok().body("Wallet successfully deleted");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
