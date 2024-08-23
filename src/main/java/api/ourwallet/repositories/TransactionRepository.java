package api.ourwallet.repositories;

import api.ourwallet.domains.Transaction;
import api.ourwallet.domains.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<List<Transaction>> findTransactionsByWallet(Wallet wallet);
}
