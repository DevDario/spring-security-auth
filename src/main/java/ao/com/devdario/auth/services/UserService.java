package ao.com.devdario.auth.services;

import ao.com.devdario.auth.entities.User;
import ao.com.devdario.auth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
