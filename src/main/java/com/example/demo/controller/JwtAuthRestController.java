package com.example.demo.controller;

import com.example.demo.details.UserDetailsImpl;
import com.example.demo.details.UserDetailsServiceImpl;
import com.example.demo.exception.CustomAuthenticationException;
import com.example.demo.jwt.JwtTokenRequest;
import com.example.demo.jwt.JwtTokenResponse;
import com.example.demo.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class JwtAuthRestController {


    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtAuthRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                 UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "${jwt.get.token.uri}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
            throws CustomAuthenticationException {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();

        jwtTokenResponse.setToken(token);
        return ResponseEntity.ok().body(jwtTokenResponse.getToken());
    }

    @GetMapping(value = "${jwt.refresh.token.uri}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
            jwtTokenResponse.setToken(refreshedToken);
            return ResponseEntity.ok(jwtTokenResponse.getToken());
        } else {
            return ResponseEntity.badRequest().body("");
        }
    }

}
