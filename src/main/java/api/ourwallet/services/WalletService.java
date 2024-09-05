package api.ourwallet.services;

import api.ourwallet.domains.User;
import api.ourwallet.domains.Wallet;
import api.ourwallet.repositories.UserRepository;
import api.ourwallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;


    public Wallet createWallet(String userEmail) {
        User user = this.userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = new Wallet();
        wallet.setBalance(0.0);
        Wallet newWallet = this.walletRepository.save(wallet);
        user.addWallet(newWallet);

        this.userRepository.save(user);

        return newWallet;
    }

    public void deleteWallet(String walletId) {
        this.walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

}
