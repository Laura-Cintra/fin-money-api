package br.com.fiap.fin_money_api.repository;

import br.com.fiap.fin_money_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Repositório JPA para a entidade Category
}