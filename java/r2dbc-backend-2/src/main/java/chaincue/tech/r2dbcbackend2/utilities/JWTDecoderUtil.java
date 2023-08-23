package chaincue.tech.r2dbcbackend2.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTDecoderUtil {

    public static String getSubIdFromToken(String token) {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64.Decoder base64Url = Base64.getUrlDecoder();

        String body = new String(base64Url.decode(base64EncodedBody));

        try {
            JSONObject jsonObject = new JSONObject(body);
            return jsonObject.get("sub").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createToken(String subId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "email@email.com");
        claims.put("preferred_username", "email@email.com");

        Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        long expirationMillis = 864_000_000;

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(jwtKey)
                .compact();
        return "Bearer " + token;
    }
}
