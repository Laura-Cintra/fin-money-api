package br.com.fiap.fin_money_api.specification;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.fin_money_api.controller.TransactionController.TransactionFilters;
import br.com.fiap.fin_money_api.model.Transaction;
import jakarta.persistence.criteria.Predicate;

public class TransactionSpecification {
    public static Specification<Transaction> withFilters(TransactionFilters filters){ // recebe os filtros e transforma em uma espeficicação pro BD
        return (root, query, cb) -> { // cb --> critery builder // expressão lambda == Arrow function no JS 
            
            var predicates = new ArrayList<>(); // array de predicados ("where") - cada predicado que eu quiser utilizar, eu adiciono na lista
            
            if(filters.description() != null){
                predicates.add(
                cb.like( // predicado
                    cb.lower(root.get("description")), "%" + filters.description().toLowerCase() + "%")
                ); // critérios de busca
            }

            // busca transações entre a data de início e data de fim
            if(filters.startDate() != null && filters.endDate() != null){ // tem a data inicial e a final
                predicates.add(
                    cb.between(root.get("date"), filters.startDate(), filters.endDate())
                );
            }

            // busca transações com a data igual a data de ínicio
            if(filters.startDate() != null && filters.endDate() == null){ // tem a data inicial e não tem a final
                predicates.add(
                    cb.equal(root.get("date"), filters.startDate())
                );
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]); // pega todos os predicados adicionados no array e jogo em um array de predicados
            return cb.and(arrayPredicates); // cb.and() --> devolve um ou mais predicados
        }; 
    }
} 