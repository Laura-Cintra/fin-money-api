package br.com.fiap.fin_money_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.fin_money_api.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{ // model, tipo da chave primária
    // Repositório JPA para a entidade Transaction
}
