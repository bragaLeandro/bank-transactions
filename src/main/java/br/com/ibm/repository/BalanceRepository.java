package br.com.ibm.repository;

import br.com.ibm.entity.Balance;
import br.com.ibm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    Balance findByUser(User user);
}