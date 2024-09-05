package api.ourwallet.services;

import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.Wallet;
import api.ourwallet.repositories.TransactionRepository;
import api.ourwallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;


    public Transaction createTransaction(Transaction transaction,  String walletId) {
        Wallet wallet = this.walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        if(transaction.getType().equals("SAQUE")) {
            wallet.setBalance(wallet.getBalance() - transaction.getValue());
        }else{
            wallet.setBalance(wallet.getBalance() + transaction.getValue());
        }

        Wallet walletUpdated = this.walletRepository.save(wallet);

        transaction.setWallet(walletUpdated);

        Transaction newTransaction = this.transactionRepository.save(transaction);

        if(newTransaction.getId().isEmpty()) {
            throw new RuntimeException("Transaction cannot be created");
        }
        return newTransaction;
    }


    public List<Transaction> findAllTransactionByWallet(String walletId) {
        Wallet wallet = this.walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        List<Transaction> transactions = this.transactionRepository.findTransactionsByWallet(wallet).orElseThrow(() -> new RuntimeException("No transactions found"));
        return transactions;
    }



}
