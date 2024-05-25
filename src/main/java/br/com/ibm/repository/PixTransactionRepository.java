package br.com.ibm.repository;

import br.com.ibm.entity.PixTransaction;
import br.com.ibm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PixTransactionRepository extends JpaRepository<PixTransaction, UUID> {

    List<PixTransaction> findPixTransactionByCreditor(User user);
    List<PixTransaction> findPixTransactionByDebitor(User user);
}