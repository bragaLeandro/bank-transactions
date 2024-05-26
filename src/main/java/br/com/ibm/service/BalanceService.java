package br.com.ibm.service;

import br.com.ibm.dto.BalanceDto;
import br.com.ibm.entity.Balance;
import br.com.ibm.entity.User;
import br.com.ibm.repository.BalanceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService (BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }
    public Balance setInicialBalance(User user) {
        Balance initialBalance = new Balance();
        initialBalance.setUser(user);
        initialBalance.setValue(BigDecimal.valueOf(1000L));
        return initialBalance;
    }

    public BalanceDto getBalance() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Balance balance = balanceRepository.findByUser(user);
        return new BalanceDto(balance.getValue());
    }
}
