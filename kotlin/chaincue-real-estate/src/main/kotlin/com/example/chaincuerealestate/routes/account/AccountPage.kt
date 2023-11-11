package com.example.chaincuerealestate.routes.account

import com.example.chaincuerealestate.routes.home.HomePage
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("account")
class AccountPage {

    @GetMapping()
    fun homePage(@RequestHeader("Authorization") token: String): ResponseEntity<AccountPageDTO> {
        log.info("HomePage")
        log.info(token)
        val toDTO = toHomePageDTO { it }
        return ResponseEntity.ok(toDTO)
    }

    private fun toHomePageDTO(additionalProcessing: ((DTOBuilder) -> DTOBuilder)?): AccountPageDTO {
        return (additionalProcessing?.invoke(DTOBuilder("")) ?: DTOBuilder(""))
            .let { toDTO(it) }
    }

    private fun toDTO(dtoBuilder: DTOBuilder): AccountPageDTO {
        return AccountPageDTO(
            id = dtoBuilder.id
        )
    }

    private data class DTOBuilder(
        val id: String = ""
    )

    private companion object {
        private val log = LoggerFactory.getLogger(HomePage::class.java)
    }
}
