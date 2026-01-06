package in.swarnavo.ecommerce_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;

    @NotBlank
    private String paymentMethod;

    private String pgName;
    private String pgPaymentId;
    private String pgOrderId;
    private String pgStatus;
    private String pgSignature;
    private String pgResponseMessage;

    private Double amount;
    private String currency;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Payment(String paymentMethod, String pgName, String pgOrderId, Double amount, String currency) {
        this.paymentMethod = paymentMethod;
        this.pgName = pgName;
        this.pgOrderId = pgOrderId;
        this.amount = amount;
        this.currency = currency;
        this.pgStatus = "created";
    }

    public Payment(String paymentMethod, Double amount, String currency) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
        this.pgStatus = "pending";
    }
}
