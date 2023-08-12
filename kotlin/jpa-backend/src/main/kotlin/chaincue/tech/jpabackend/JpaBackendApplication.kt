package chaincue.tech.jpabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaBackendApplication

fun main(args: Array<String>) {
	runApplication<JpaBackendApplication>(*args)
}
