package br.com.ibm.controller;


import br.com.ibm.dto.TransactionDto;
import br.com.ibm.dto.DetailedTransactionDto;
import br.com.ibm.dto.TransactionResponseDto;
import br.com.ibm.exception.InactiveUserException;
import br.com.ibm.exception.InsufficientBalanceException;
import br.com.ibm.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> makeTransfer(@RequestBody TransactionDto transactionDto) {
        try {
            this.transactionService.makeTransference(transactionDto);
            return ResponseEntity.ok(new TransactionResponseDto("Success", "Transferência Finalizada"));
        } catch (InactiveUserException | InsufficientBalanceException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TransactionResponseDto("Failed", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new TransactionResponseDto("Failed", ex.getMessage()));

        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<DetailedTransactionDto>> getHistoryTransactions() {
        List<DetailedTransactionDto> transactions = this.transactionService.getHistoryTransactions();
        return ResponseEntity.ok(transactions);
    }
}
