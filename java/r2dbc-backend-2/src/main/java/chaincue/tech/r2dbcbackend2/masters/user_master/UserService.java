package chaincue.tech.r2dbcbackend2.masters.user_master;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UserService implements UserContract {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

}
