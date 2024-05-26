package br.com.ibm.controller;


import br.com.ibm.dto.PixTransactionDto;
import br.com.ibm.dto.TransactionPixTransactionDto;
import br.com.ibm.exception.InactiveUserException;
import br.com.ibm.exception.InsufficientBalanceException;
import br.com.ibm.service.PixTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final PixTransactionService pixTransactionService;

    public TransactionController(PixTransactionService pixTransactionService) {
        this.pixTransactionService = pixTransactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> makeTransfer(@RequestBody PixTransactionDto pixTransactionDto) {
        try {
            this.pixTransactionService.makeTransference(pixTransactionDto);
            return ResponseEntity.ok("Transferência finalizada");
        } catch (InactiveUserException | InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionPixTransactionDto>> getHistoryTransactions() {
        List<TransactionPixTransactionDto> transactions = this.pixTransactionService.getHistoryTransactions();
        return ResponseEntity.ok(transactions);
    }
}
