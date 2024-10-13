package ao.com.devdario.auth.services;

import ao.com.devdario.auth.dtos.LoginUserDto;
import ao.com.devdario.auth.dtos.RegisterUserDto;
import ao.com.devdario.auth.entities.User;
import ao.com.devdario.auth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto registerUserDto){
        User user = User.builder()
                .fullname(registerUserDto.getFullName())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .email(registerUserDto.getEmail())
                .build();

        return  userRepository.save(user);
    }

    public User authenticate(LoginUserDto loginUserDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(),loginUserDto.getPassword())
        );

        return  userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
    }

}
