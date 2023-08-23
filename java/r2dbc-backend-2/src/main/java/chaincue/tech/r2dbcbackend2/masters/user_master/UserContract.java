package chaincue.tech.r2dbcbackend2.masters.user_master;

import reactor.core.publisher.Mono;

public interface UserContract {
    Mono<User> saveUser(User user);
}
