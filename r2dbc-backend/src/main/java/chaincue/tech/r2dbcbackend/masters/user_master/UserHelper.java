package chaincue.tech.r2dbcbackend.masters.user_master;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UserHelper {
    private final UserRepository userRepository;

    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

}
