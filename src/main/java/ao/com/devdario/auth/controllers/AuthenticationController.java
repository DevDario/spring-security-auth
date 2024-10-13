package ao.com.devdario.auth.controllers;

import ao.com.devdario.auth.dtos.LoginResponseDto;
import ao.com.devdario.auth.dtos.LoginUserDto;
import ao.com.devdario.auth.dtos.RegisterUserDto;
import ao.com.devdario.auth.entities.User;
import ao.com.devdario.auth.security.JwtService;
import ao.com.devdario.auth.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterUserDto> register(@RequestBody RegisterUserDto registerUserDto){
        User registeredUser = authenticationService.signup(registerUserDto);
        RegisterUserDto userDTO = RegisterUserDto.builder()
                .email(registeredUser.getEmail())
                .fullName(registeredUser.getFullname())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .userId(authenticatedUser.getId())
                .username(authenticatedUser.getUsername())
                .token(jwtToken)
                .expiredIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
}
