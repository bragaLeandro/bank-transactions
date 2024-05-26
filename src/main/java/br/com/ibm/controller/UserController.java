package br.com.ibm.controller;

import br.com.ibm.dto.BalanceDto;
import br.com.ibm.dto.LoginDto;
import br.com.ibm.dto.UserDto;
import br.com.ibm.entity.Balance;
import br.com.ibm.entity.User;
import br.com.ibm.service.BalanceService;
import br.com.ibm.service.TokenService;
import br.com.ibm.service.UserService;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(User.class);
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final BalanceService balanceService;

    public UserController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UserService userService,
                          BalanceService balanceService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.balanceService = balanceService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        try {
            this.userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto login) {
        logger.info("Calling Service(GET) /login {}", login.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(),
                        login.getPassword());

        Authentication authenticate = this.authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

        var user = (User) authenticate.getPrincipal();
        logger.info(String.valueOf(user));
        return tokenService.generateToken(user);
    }

    @PutMapping("/inactivate")
    public ResponseEntity<String> inactivateUser(@RequestParam("id") Long id) {
        try {
            this.userService.inactivateUser(id);
            return ResponseEntity.ok("User deactivated");
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("id") Long id) {
        try {
            this.userService.activateUser(id);
            return ResponseEntity.ok("User activated");
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> getBalance() {
        return ResponseEntity.ok(balanceService.getBalance());
    }
}