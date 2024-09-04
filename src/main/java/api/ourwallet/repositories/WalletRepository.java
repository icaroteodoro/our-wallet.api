package api.ourwallet.repositories;

import api.ourwallet.domains.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, String> {
}
