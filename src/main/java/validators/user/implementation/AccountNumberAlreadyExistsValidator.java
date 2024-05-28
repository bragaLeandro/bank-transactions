package validators.user.implementation;

import br.com.ibm.entity.User;
import validators.user.UserValidator;

public class AccountNumberAlreadyExistsValidator extends UserValidator {

    @Override
    public void validate(User user) {
        if (this.accountAlreadyExists(user.getAccountNumber()))
            throw new IllegalArgumentException("There is already an account with this number");
    }

    public boolean accountAlreadyExists(String accountNumber) {
        return userRepository.findByAccountNumber(accountNumber).isPresent();
    }
}
