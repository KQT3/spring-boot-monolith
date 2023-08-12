package chaincue.tech.jpabackend

import chaincue.tech.jpabackend.utilities.ANSIColors
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JpaBackendApplicationTests {

	@Test
	fun contextLoads() {
		ANSIColors.printGreen("JpaBackendApplicationTests")
	}

}
