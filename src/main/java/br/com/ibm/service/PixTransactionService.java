package br.com.ibm.service;

import br.com.ibm.dto.PixTransactionDto;
import br.com.ibm.dto.TransactionPixTransactionDto;
import br.com.ibm.entity.Balance;
import br.com.ibm.entity.PixTransaction;
import br.com.ibm.entity.User;
import br.com.ibm.exception.InactiveUserException;
import br.com.ibm.exception.InsufficientBalanceException;
import br.com.ibm.repository.BalanceRepository;
import br.com.ibm.repository.PixKeyRepository;
import br.com.ibm.repository.PixTransactionRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PixTransactionService {

    private final UserService userService;
    private final BalanceRepository balanceRepository;
    private final PixKeyRepository pixKeyRepository;
    private final PixTransactionRepository pixTransactionRepository;
    private final PixKeyService pixKeyService;
    private final ModelMapper modelMapper;

    public PixTransactionService(UserService userService,
                                 BalanceRepository balanceRepository,
                                 PixKeyRepository pixKeyRepository,
                                 PixTransactionRepository pixTransactionRepository,
                                 PixKeyService pixKeyService,
                                 ModelMapper modelMapper) {
        this.userService = userService;
        this.balanceRepository = balanceRepository;
        this.pixKeyRepository = pixKeyRepository;
        this.pixTransactionRepository = pixTransactionRepository;
        this.pixKeyService = pixKeyService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void makeTransference(PixTransactionDto pixTransactionDto) {
        try {
            validateUsersAndBalances(pixTransactionDto);
            processTransference(pixTransactionDto);
        } catch (InactiveUserException | InsufficientBalanceException e) {
            handleFailedTransaction(pixTransactionDto, e.getMessage());
            throw e;
        }
    }

    private void validateUsersAndBalances(PixTransactionDto transacaoPixDto) {

        User debitorUser = pixKeyService.findPixKeyByKeyValue(transacaoPixDto.getDebitor().getKeyValue()).getUser();
        User creditorUser = pixKeyService.findPixKeyByKeyValue(transacaoPixDto.getCreditor().getKeyValue()).getUser();

        if (!debitorUser.isEnabled() || !creditorUser.isEnabled()) {
            throw new InactiveUserException("Both users need to be active");
        }

        Balance debitorBalance = debitorUser.getBalance();
        if (debitorBalance.getValue().compareTo(BigDecimal.ZERO) <= 0 || debitorBalance.getValue().compareTo(transacaoPixDto.getTransferValue()) < 0) {
            throw new InsufficientBalanceException("Balance unavailable");
        }
    }

    private void processTransference(PixTransactionDto pixTransactionDto) {
        User debitorUser = pixKeyService.findPixKeyByKeyValue(pixTransactionDto.getDebitor().getKeyValue()).getUser();
        User creditorUser = pixKeyService.findPixKeyByKeyValue(pixTransactionDto.getCreditor().getKeyValue()).getUser();

        Balance debitorBalance = debitorUser.getBalance();
        Balance creditorBalance = creditorUser.getBalance();

        debitorBalance.setValue(debitorBalance.getValue().subtract(pixTransactionDto.getTransferValue()));
        creditorBalance.setValue(creditorBalance.getValue().add(pixTransactionDto.getTransferValue()));

        PixTransaction pixTransaction = new PixTransaction(debitorUser, creditorUser, "Success", pixTransactionDto.getTransferValue());

        List<Balance> modifiedBalances = Arrays.asList(debitorBalance, creditorBalance);

        pixTransactionRepository.save(pixTransaction);
        balanceRepository.saveAll(modifiedBalances);
    }

    private void handleFailedTransaction(PixTransactionDto pixTransactionDto, String failureReason) {
        User debitorUser = pixKeyService.findPixKeyByKeyValue(pixTransactionDto.getDebitor().getKeyValue()).getUser();
        User creditorUser = pixKeyService.findPixKeyByKeyValue(pixTransactionDto.getCreditor().getKeyValue()).getUser();

        PixTransaction transacaoPix = new PixTransaction(debitorUser, creditorUser, "Failed", pixTransactionDto.getTransferValue());
        transacaoPix.setDescription(failureReason);
        pixTransactionRepository.save(transacaoPix);
    }

    public List<TransactionPixTransactionDto> getHistoryTransactions() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PixTransactionDto> transactions = new ArrayList<>();

        List<PixTransaction> creditor = pixTransactionRepository.findPixTransactionByCreditor(user);
        List<PixTransaction> debitor = pixTransactionRepository.findPixTransactionByDebitor(user);

        List<PixTransactionDto> creditorDto = creditor.stream()
                .map(transaction -> modelMapper.map(transaction, PixTransactionDto.class)).toList();

        List<PixTransactionDto> debitorDto = debitor.stream()
                .map(transaction -> modelMapper.map(transaction, PixTransactionDto.class)).toList();

        transactions.addAll(creditorDto);
        transactions.addAll(debitorDto);

        List<TransactionPixTransactionDto> detailedTransactions = transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionPixTransactionDto.class)).toList();

        return detailedTransactions;
    }



}