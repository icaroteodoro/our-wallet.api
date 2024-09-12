package api.ourwallet.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double value;
    private String type;
    private LocalDateTime createdAt;

    @ManyToOne(cascade=CascadeType.ALL)
    private Wallet wallet;

    @ManyToOne(cascade=CascadeType.ALL)
    private Category category;
}