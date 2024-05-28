package br.com.ibm.service;

import br.com.ibm.dto.TransactionDto;
import br.com.ibm.dto.DetailedTransactionDto;
import br.com.ibm.entity.Balance;
import br.com.ibm.entity.Transaction;
import br.com.ibm.entity.User;
import br.com.ibm.exception.InactiveUserException;
import br.com.ibm.exception.InsufficientBalanceException;
import br.com.ibm.repository.BalanceRepository;
import br.com.ibm.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class TransactionService {

    private final UserService userService;
    private final BalanceRepository balanceRepository;

    private final TransactionRepository transactionRepository;

    private final ModelMapper modelMapper;

    public TransactionService(UserService userService,
                              BalanceRepository balanceRepository,
                              TransactionRepository transactionRepository,
                              ModelMapper modelMapper) {
        this.userService = userService;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void makeTransference(TransactionDto transactionDto) {
        try {
            if ("DEBIT".equalsIgnoreCase(transactionDto.getType())) {
                validateUsersAndBalances(transactionDto);
                processTransference(transactionDto);
            } else if ("CREDIT".equalsIgnoreCase(transactionDto.getType())) {
                processCreditTransference(transactionDto);
            }
        } catch (InactiveUserException | InsufficientBalanceException | IllegalArgumentException e) {
            handleFailedTransaction(transactionDto, e.getMessage());
            throw e;
        }
    }

    private void validateUsersAndBalances(TransactionDto transactionDto) {

        User sender = userService.findByAccountNumber(transactionDto.getSenderAccountNumber());
        User receiver = userService.findByAccountNumber(transactionDto.getDestinationAccountNumber());

        if (!sender.isEnabled() || !receiver.isEnabled()) {
            throw new InactiveUserException("Both users need to be active");
        }

        Balance senderBalance = sender.getBalance();

        if (senderBalance.getValue().compareTo(BigDecimal.ZERO) <= 0 || senderBalance.getValue().compareTo(transactionDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Balance unavailable");
        }
    }

    private void processTransference(TransactionDto transactionDto) {

        User sender = userService.findByAccountNumber(transactionDto.getSenderAccountNumber());
        User receiver = userService.findByAccountNumber(transactionDto.getDestinationAccountNumber());

        Balance senderBalance = sender.getBalance();
        Balance receiverBalance = receiver.getBalance();

        senderBalance.setValue(senderBalance.getValue().subtract(transactionDto.getAmount()));
        receiverBalance.setValue(receiverBalance.getValue().add(transactionDto.getAmount()));

        Transaction transaction = new Transaction(sender, receiver, transactionDto.getType(), "Success", transactionDto.getAmount());

        List<Balance> modifiedBalances = Arrays.asList(senderBalance, receiverBalance);

        transactionRepository.save(transaction);
        balanceRepository.saveAll(modifiedBalances);
    }

    private void processCreditTransference(TransactionDto transactionDto) {
        User creditorUser = userService.findByAccountNumber(transactionDto.getSenderAccountNumber());
        Balance creditorBalance = balanceRepository.findByUser(creditorUser);

        creditorBalance.setValue(creditorBalance.getValue().add(transactionDto.getAmount()));

        Transaction transaction = new Transaction(null, creditorUser, transactionDto.getType(), "Success", transactionDto.getAmount());

        transactionRepository.save(transaction);
        balanceRepository.save(creditorBalance);
    }

    private void handleFailedTransaction(TransactionDto transactionDto, String failureReason) {

        User sender = userService.findByAccountNumber(transactionDto.getSenderAccountNumber());
        User receiver = userService.findByAccountNumber(transactionDto.getDestinationAccountNumber());

        Transaction transaction = new Transaction(sender, receiver, transactionDto.getType(), "Failed", transactionDto.getAmount());
        transaction.setDescription(failureReason);
        transactionRepository.save(transaction);
    }

    public List<DetailedTransactionDto> getHistoryTransactions() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TransactionDto> transactions = new ArrayList<>();

        List<Transaction> creditor = transactionRepository.findTransactionByCreditorOrderByTransactionDateDesc(user);
        List<Transaction> debitor = transactionRepository.findTransactionByDebitorOrderByTransactionDateDesc(user);

        List<TransactionDto> creditorDto = creditor.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class)).toList();

        List<TransactionDto> debitorDto = debitor.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class)).toList();

        transactions.addAll(creditorDto);
        transactions.addAll(debitorDto);

        List<DetailedTransactionDto> detailedTransactions = transactions.stream()
                .map(transaction -> modelMapper.map(transaction, DetailedTransactionDto.class)).toList();

        return detailedTransactions;
    }



}