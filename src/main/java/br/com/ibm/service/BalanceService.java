package br.com.ibm.service;

import br.com.ibm.entity.Balance;
import br.com.ibm.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {

    public Balance setInicialBalance(User user) {
        Balance initialBalance = new Balance();
        initialBalance.setUser(user);
        initialBalance.setValue(BigDecimal.ZERO);
        return initialBalance;
    }
}
