package validators.user.implementation;

import br.com.ibm.entity.User;
import org.springframework.stereotype.Component;
import validators.user.UserValidator;

@Component
public class EmailAlreadyExistsValidator extends UserValidator {

    @Override
    public void validate(User user) {
        if (this.emailAlreadyExists(user.getEmail()))
            throw new IllegalArgumentException("Email already registered");
    }
    public boolean emailAlreadyExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }
}
