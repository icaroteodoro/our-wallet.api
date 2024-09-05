package api.ourwallet.controllers;

import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.Wallet;
import api.ourwallet.dtos.TransactionRequestDTO;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.WalletRepository;
import api.ourwallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create/{id}")
    public ResponseEntity setTransaction(@RequestBody TransactionRequestDTO body, @PathVariable String id) {
        try{
            Transaction newTransaction = new Transaction();
            newTransaction.setValue(body.value());
            newTransaction.setType(body.type());
            newTransaction.setCreatedAt(LocalDateTime.now());

            this.transactionService.createTransaction(newTransaction, id);

            return ResponseEntity.ok().body("Transaction created successfully");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{walletId}")
    public ResponseEntity findAllTransactionByWallet(@PathVariable String walletId) {
        try {
            List<Transaction> transactions = this.transactionService.findAllTransactionByWallet(walletId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No transactions found");
        }
    }
}
