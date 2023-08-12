package chaincue.tech.jpabackend.masters.user_master

import chaincue.tech.jpabackend.utilities.ANSIColors
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import java.io.File

@SpringBootTest
class UserHelperTest(@Autowired val userHelper: UserHelper) {
    companion object {
        @Container
        val serverContainer = DockerComposeContainer(File("src/main/resources/docker-compose-test.yml"))
    }

    @Test
    fun save() {
        val username = "username"
        val user = User.create(username)
        val result = userHelper.save(user)
        ANSIColors.printBlue(result)
        assertEquals(username, result.userName)
    }

    @Test
    fun findUser() {
    }
}
