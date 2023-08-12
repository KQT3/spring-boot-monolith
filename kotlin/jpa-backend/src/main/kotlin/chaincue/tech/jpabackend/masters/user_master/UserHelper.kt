package chaincue.tech.jpabackend.masters.user_master

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserHelper(private val userRepository: UserRepository) {
    fun save(user: User): User = userRepository.save(user)
    fun findUser(id: String): Optional<User> = userRepository.findById(id)
}
