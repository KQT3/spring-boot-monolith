package chaincue.tech.jpabackend.masters.user_master;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserHelper {
    private final UserRepository userRepository;

    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(String id) {
        return userRepository.findById(id);
    }

}
