package api.ourwallet.domains;


import api.ourwallet.dtos.RegisterRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Wallet> wallets = new ArrayList<>();

    public void addWallet (Wallet wallet) {
        this.wallets.add(wallet);
    }


    public User(RegisterRequestDTO body) {
        this.setName(body.name());
        this.setEmail(body.email());
        this.setPassword(body.password());
    }
}
