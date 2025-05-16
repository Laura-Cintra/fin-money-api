package br.com.fiap.fin_money_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // mapear a classe para uma tabela no banco de dados
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @NotBlank(message = "campo obrigatório")
    @Size(min = 5, max = 255, message = "deve ter pelo menos 5 caracteres")
    private String description;

    @Positive(message = "deve ser positivo")
    private BigDecimal amount;

    @PastOrPresent(message = "não pode ser no futuro")
    @JsonIgnore
    private LocalDate date;

    @NotNull(message = "campo obrigatório")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull(message = "campo obrigatório")
    @ManyToOne
    private Category category;
}
