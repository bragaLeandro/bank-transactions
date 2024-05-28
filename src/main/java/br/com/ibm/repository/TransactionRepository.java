package br.com.ibm.repository;

import br.com.ibm.entity.Transaction;
import br.com.ibm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findTransactionByCreditorOrderByTransactionDateDesc(User user);
    List<Transaction> findTransactionByDebitorOrderByTransactionDateDesc(User user);
}