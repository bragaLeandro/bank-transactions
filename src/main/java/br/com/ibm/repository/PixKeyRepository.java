package br.com.ibm.repository;

import br.com.ibm.entity.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {
}