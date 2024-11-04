package backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
//import javax.crypto.KeyGenerator;
//import javax.crypto.Mac;
import javax.crypto.SecretKey;

//import javax.crypto.spec.SecretKeySpec;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secretkey}")
    private String secretkey;

//    public JwtService() {
//
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.freshTokenExpiration}")
    private long freshTokenExpiration;

    /**
     * 生成token
     * @param username 用户名
     * @param type token类型 1: access token 2: fresh token
     * @return token
     */
    public String generateToken(String username, Integer type) {
        long expiration = type == 1 ? accessTokenExpiration : freshTokenExpiration;
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getKey())
                .compact();

    }

    /**
     * 获取密钥
     * @return 密钥
     */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从token中提取用户名
     * @param token token
     * @return 用户名
     */

    public String extractUserName(String token) {
        // extract the username from jwt token
        try {
            return extractClaim(token, Claims::getSubject);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    /**
     * 从token中提取过期时间
     * @param token token
     * @return 过期时间
     */

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * 从token中提取所有声明
     * @param token token
     * @return 所有声明
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }

    }

    /**
     * 验证token
     * @param token token
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 判断token是否过期
     * @param token token
     * @return 是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 从token中提取过期时间
     * @param token token
     * @return 过期时间
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
