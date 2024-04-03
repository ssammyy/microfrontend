import com.ngumo.inventoryapi.Configurations.Security.UserPrincipal
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.Date

@Service
 class TokenProvider{
//    @Value("\${app.jwtSecret}")
//    private val jwtSecret: String = ""
//
//    @Value("\${app.jwtExpirationMs}")
//    private val jwtExpirationMs: Long

    val jwtSecret : String = "client-secret"
    val jwtExpirationMs : Long = 30000000

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(now)
//            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }
}
