package org.assetlifecyclemanagement.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> generateToken(@RequestBody AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(), authRequest.getPassword()));
            return new ResponseEntity<>(jwtUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

}
