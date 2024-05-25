package br.com.ibm.repository;

import br.com.ibm.entity.PixTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PixTransactionRepository extends JpaRepository<PixTransaction, UUID> {
}