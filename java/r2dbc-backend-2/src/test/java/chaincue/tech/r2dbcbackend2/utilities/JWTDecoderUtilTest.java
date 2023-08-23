package chaincue.tech.r2dbcbackend2.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JWTDecoderUtilTest {

    @Test
    void decodeJWTToken() {
        String token = JWTDecoderUtil.createToken("");
        String tokenDecoded = JWTDecoderUtil.getSubIdFromToken(token);
        System.out.println(token);
        System.out.println(tokenDecoded);
        assertNotNull(tokenDecoded);
    }

    @Test
    void createToken() {
        String token = JWTDecoderUtil.createToken("");
        System.out.println(token);
        assertNotNull(token);
    }
}
