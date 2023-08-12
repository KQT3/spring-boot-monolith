package chaincue.tech.jpabackend.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.*;

public class JWTDecoderUtil {

    public static String decodeJWTToken(String token) {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64.Decoder base64Url = Base64.getUrlDecoder();

        String body = new String(base64Url.decode(base64EncodedBody));

        try {
            JSONObject jsonObject = new JSONObject(body);
            return jsonObject.get("email").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeJWTToken(String token, String secretKey) throws Exception {
//        Base64.Decoder decoder = Base64.getUrlDecoder();
//
//        String[] chunks = token.split("\\.");
//
//        String header = new String(decoder.decode(chunks[0]));
//        String payload = new String(decoder.decode(chunks[1]));
//
//        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
//        String signature = chunks[2];
//
//        SignatureAlgorithm sa = HS256;
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
//
//        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
//
//        if (!validator.isValid(tokenWithoutSignature, signature)) {
//            throw new Exception("Could not verify JWT token integrity!");
//        }
//
//        return header + " " + payload;
        return token;
    }

    @Value
    private static class RealmAccess {
        String[] roles;
    }

    @Value
    private static class resourceAccess {
        Account account;

        @Value
        private static class Account {
            String[] roles;
        }
    }

//    public static String createToken(String username) {
//        ArrayList<String> roles = new ArrayList<>();
//        roles.add("student");
//        roles.add("teacher");
//        roles.add("offline_access");
//        roles.add("admin");
//        roles.add("uma_authorization");
//        RealmAccess realm_access = new RealmAccess(roles.toArray(String[]::new));
//
//        ArrayList<String> resourceAccessRoles = new ArrayList<>();
//        resourceAccessRoles.add("manage-account");
//        resourceAccessRoles.add("manage-account-links");
//        resourceAccessRoles.add("view-profile");
//        resourceAccess.Account account = new resourceAccess.Account(resourceAccessRoles.toArray(String[]::new));
//        resourceAccess resourceAccess = new resourceAccess(account);
//
//        ArrayList<String> allowedOrigins = new ArrayList<>();
//        allowedOrigins.add("*");
//
//        Claims claims = Jwts.claims();
//        claims.setSubject(username);
//        claims.put("email", username);
//        claims.put("preferred_username", username);
//        claims.put("iss", "https://iam.sensera.se/auth/realms/lambda");
//        claims.put("aud", "account");
//        claims.put("typ", "Bearer");
//        claims.put("azp", "teacher-portal-client");
//        claims.put("scope", "openid email profile");
//        claims.put("acr", "0");
//        claims.put("allowed-origins", allowedOrigins);
//        claims.put("realm_access", realm_access);
//        claims.put("resource_access", resourceAccess);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
//                .signWith(SignatureAlgorithm.HS512, "ThisIsASecret")
//                .compact();
//    }

    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", username);
        claims.put("preferred_username", username);

        Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        long expirationMillis = 864_000_000;

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(jwtKey)
                .compact();

        return token;
    }
}
