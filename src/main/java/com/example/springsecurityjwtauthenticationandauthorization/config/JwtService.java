package com.example.springsecurityjwtauthenticationandauthorization.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

        private static final String SECRET_KEY = "743777217A24432646294A404E635266556A586E3272357538782F413F442A47"; // JWT token'ı oluşturmak için kullanılacak anahtar.

        // JWT token'ı oluşturma
        public String generateToken(String email) {
            return null;
        }

         // JWT token'ı içerisindeki email'i doğrulama
        public boolean validateToken(String token, String email) {
            return false;
        }

        // JWT token'ı içerisindeki email'i çıkarma
        // extractClaim() metodu içerisindeki Jwts sınıfı içerisindeki getBody() metodu ile JWT token'ı içerisindeki bilgileri çıkarmak için kullanılır.
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }


        // JWT token'ı oluşturma
        public String generateToken(UserDetails userDetails) {
            return generateToken(new HashMap<>(), userDetails);
        }


        // JWT token'ı içerisindeki kullanıcı bilgilerinin son tarih bilgisini çıkarma
        // extractClaim() metodu içerisindeki Jwts sınıfı içerisindeki getBody() metodu ile JWT token'ı içerisindeki bilgileri çıkarmak için kullanılır.
        // Jwts sınıfı içerisindeki builder() metodu, JWT token'ı oluşturmak için kullanılır.
        // JWT token'ı içerisindeki tüm bilgileri ekliyoruz.
        // Jwts sınıfı içerisindeki setClaims() metodu, JWT token'ı içerisindeki tüm bilgileri eklemek için kullanılır.
        // Jwts sınıfı içerisindeki setSubject() metodu, JWT token'ı içerisindeki email bilgisini eklemek için kullanılır.
        // Jwts sınıfı içerisindeki setIssuedAt() metodu, JWT token'ı oluşturulma tarihini eklemek için kullanılır.
        // Jwts sınıfı içerisindeki setExpiration() metodu, JWT token'ı son kullanma tarihini eklemek için kullanılır. Yani bu tokenının ne kadar süre ile geçerli olacağını ayarlıyoruz. Yani bu token'ı 24 saat içerisinde kullanmak zorundasın. 24 saat sonra bu token'ı kullanamazsın anlamında.
        // Jwts sınıfı içerisindeki signWith() metodu, JWT token'ı oluştururken kullanılacak anahtar bilgisini ve algoritmayı belirtmek için kullanılır. Bu anahtar bilgisi ile JWT token'ı oluşturulur-çözülür-doğrulanır-silinir.
        public String generateToken(
                Map<String, Object> extraClaims,
                UserDetails userDetails
        ) {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }


        // Sahip olunan kullanıcı adının giriş işleminde sahip olduğumuz kullanıcı adına yani email ile aynı olduğundan emin olmak için kullanılır. Yani bu token'ı kimin oluşturduğunu doğrulamak için kullanılır.
       // Ve tabi isTokenExpired() metodu ile de token'ın süresinin dolup dolmadığını kontrol ediyoruz.
        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        }

        // isTokenExpired() metodu ile de token'ın süresinin dolup dolmadığını kontrol ediyoruz.
        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }


        // JWT token'ı içerisindeki kullanıcı bilgilerinin son tarih bilgisini çıkarma
        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }


    // JWT token'ı içerisindeki tüm bilgileri çıkarma
        // Claims sınıfı, JWT token'ı içerisindeki bilgileri tutar.
        // Jwts sınıfı, JWT token'ı oluşturmak için kullanılır.
        // Jwts sınıfı içerisindeki parserBuilder() metodu, JWT token'ı çözmek için kullanılır.
        // Jwts sınıfı içerisindeki parseClaimsJws() metodu, JWT token'ı çözmek için kullanılır.
        // Jwts sınıfı içerisindeki getBody() metodu, JWT token'ı içerisindeki bilgileri çıkarmak için kullanılır.
        // Jwts sınıfı içerisindeki getSignInKey() metodu, JWT token'ı oluştururken kullanılan secret key'i döndürür.
        private Claims extractAllClaims(String token) {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        // JWT token'ı içerisindeki tüm bilgileri çıkarma
        // Function sınıfı içerisindeki apply() metodu, extractAllClaims() metodu içerisindeki Jwts sınıfı içerisindeki getBody() metodu ile JWT token'ı içerisindeki bilgileri çıkarmak için kullanılır.
        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        // JWT token'ı oluştururken kullanılan secret key'i döndürür.
        // Decoders sınıfı, JWT token'ı oluştururken kullanılan secret key'i byte dizisine çevirmek için kullanılır.
        private Key getSignInKey() {
            byte[] apiKeySecretBytes = Decoders.BASE64.decode(SECRET_KEY); // JWT token'ı oluştururken kullanılan secret key'i byte dizisine çeviriyoruz.
            return Keys.hmacShaKeyFor(apiKeySecretBytes);
        }
}
