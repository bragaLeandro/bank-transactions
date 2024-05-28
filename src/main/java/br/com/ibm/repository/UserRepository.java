package br.com.ibm.repository;

import br.com.ibm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findByAccountNumber(String accountNumber);
}