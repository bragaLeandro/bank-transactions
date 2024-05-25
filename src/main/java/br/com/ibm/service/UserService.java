package br.com.ibm.service;

import br.com.ibm.dto.UserDto;
import br.com.ibm.entity.Balance;
import br.com.ibm.entity.Product;
import br.com.ibm.entity.User;
import br.com.ibm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, BalanceService balanceService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.balanceService = balanceService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createUser(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);

        Balance initialBalance = balanceService.setInicialBalance(user);
        user.setBalance(initialBalance);

        userRepository.save(user);
    }

    public void inactivateUser(Long id) {
        User user = this.findById(id);

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("User is already disabled");
        }

        user.setEnable(false);

        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = this.findById(id);

        if (user.isEnabled()) {
            throw new IllegalArgumentException("User is already enabled");
        }

        user.setEnable(true);

        userRepository.save(user);
    }

    public void removeProductFromUser(Long userId, UUID productId) {
        User user = this.findById(userId);

        Product product = user.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in user's products list"));

        user.getProducts().remove(product);

        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
