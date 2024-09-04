package api.ourwallet.controllers;

import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.Wallet;
import api.ourwallet.dtos.TransactionRequestDTO;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @PostMapping("/create/{id}")
    public ResponseEntity<Transaction> setTransaction(@RequestBody TransactionRequestDTO body, @PathVariable String id) {
        try{
            Wallet wallet = this.walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
            double newBalance = 0.0;
            if(body.type().equals("saque")){
                newBalance = wallet.getBalance() - body.value();
            } else{
                newBalance = wallet.getBalance() + body.value();
            }
            wallet.setBalance(newBalance);
            Wallet walletUpdated = this.walletRepository.save(wallet);
            Transaction newTransaction = new Transaction();
            newTransaction.setValue(body.value());
            newTransaction.setType(body.type());
            newTransaction.setWallet(wallet);
            newTransaction.setCreatedAt(LocalDateTime.now());
            newTransaction.setWallet(walletUpdated);
            this.transactionRepository.save(newTransaction);
            return ResponseEntity.ok().build();
        }catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/{walletId}")
    public ResponseEntity<List<Transaction>> findAllTransactionByWallet(@PathVariable String walletId) {
        try {
            Wallet wallet = this.walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
            List<Transaction> transactions = this.transactionRepository.findTransactionsByWallet(wallet).orElseThrow(() -> new RuntimeException("No transactions found"));
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
