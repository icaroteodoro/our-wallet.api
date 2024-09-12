package api.ourwallet.services;

import api.ourwallet.domains.User;
import api.ourwallet.domains.Wallet;
import api.ourwallet.repositories.CategoryRepository;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.UserRepository;
import api.ourwallet.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> findAllUUser() {
        return this.userRepository.findAll();
    }


    public User createUser(User user) throws RuntimeException {
        Optional<User> userExist = this.userRepository.findByEmail(user.getEmail());
        if(userExist.isEmpty()) {
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            Wallet wallet = new Wallet();
            wallet.setBalance(0.0);
            Wallet newWallet = this.walletRepository.save(wallet);
            newUser.addWallet(newWallet);
            newUser.setCategories(this.categoryService.standardCategories());
            return this.userRepository.save(newUser);
        }
        throw new RuntimeException("Email already registered");
    }

    public User findUserById(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public void deleteUserByEmail(String email) {
        User userExist = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Unable to delete user"));

        for(Wallet wallet : userExist.getWallets()) {
            this.walletRepository.deleteById(wallet.getId());
            this.transactionRepository.deleteTransactionsByWallet(wallet);
        }
        this.userRepository.deleteById(userExist.getId());
    }

}
