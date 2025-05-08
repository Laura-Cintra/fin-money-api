package br.com.fiap.fin_money_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.fin_money_api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    // Repositório JPA para a entidade User

    Optional<User> findByEmail(String email);
}