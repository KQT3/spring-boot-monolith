package com.chaincuerealestate.chaincuerealestate.routes.account;

import com.chaincuerealestate.chaincuerealestate.domains.Country;
import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.routes.home.HomePage;
import com.chaincuerealestate.chaincuerealestate.routes.home.HomePageDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AccountPage {

    @GetMapping
    public ResponseEntity<AccountPageDTO> homePage(@RequestHeader("Authorization") String token) {
        log.info("HomePage");
        log.info(token);
        var toDTO = toHomePageDTO(Optional.empty());
        return ResponseEntity.ok(toDTO);
    }

    private AccountPageDTO toHomePageDTO(Optional<Function<DTOBuilder, DTOBuilder>> additionalProcessing) {
        return Stream.of(new DTOBuilder())
                .map(additionalProcessing.orElseGet(Function::identity))
                .map(AccountPage::toDTO)
                .findFirst().get();
    }

    private static AccountPageDTO toDTO(DTOBuilder dtoBuilder) {
        return new AccountPageDTO("id");
    }

    @Data
    private static class DTOBuilder {

        public DTOBuilder() {
        }
    }

}
