package validators.user;
import br.com.ibm.entity.User;
import br.com.ibm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class UserValidator {

    @Autowired
    protected UserRepository userRepository;

    public abstract void validate(User user);
}
