package com.ngumo.inventoryapi.Controllers.Auth;

import com.ngumo.inventoryapi.Configurations.Security.JwtUtils;
import com.ngumo.inventoryapi.Dao.Models.User;
import com.ngumo.inventoryapi.Dao.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static java.lang.Thread.sleep;

@Controller
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class Login {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private JwtUtils jwtUtils;


    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class UserWithToken {
        private User user;
        private String token;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws InterruptedException {
        sleep(2000);

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            org.springframework.security.core.userdetails.UserDetails userDetails =
                    userDetailsService.loadUserByUsername(loginRequest.getUsername());

            String token = jwtUtils.generateToken(userDetails);
            UserWithToken userWithToken = new UserWithToken(user, token);

            return ResponseEntity.ok(userWithToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
