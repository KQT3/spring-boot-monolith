package chaincue.tech.r2dbcbackend2.masters.user_master;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;

    @Test
    void saveUser_ShouldReturnSavedUser() {
        // Given
        User expectedUser = User.createUser("", "", "");
        when(userRepository.save(expectedUser)).thenReturn(Mono.just(expectedUser));

        // When & Then
        StepVerifier.create(userService.saveUser(expectedUser))
                .assertNext(userResult -> {
                    assertEquals(expectedUser, userResult);
                    verify(userRepository).save(expectedUser);
                })
                .verifyComplete();
    }
}
