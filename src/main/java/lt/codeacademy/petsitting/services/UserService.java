package lt.codeacademy.petsitting.services;


import lt.codeacademy.petsitting.pojo.User;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser( User user ){
        return userRepository.save( user );
    }

    public Optional<User> getUserById(Long id ){
        return userRepository.findById( id );
    }

    public Optional<User> findByUsername( String username ){
        return userRepository.findByUsername( username );
    }

    public boolean existsByUsername( String username ) {
        return userRepository.existsByUsername( username );
    }

    public boolean existsByEmail( String email ) {
        return userRepository.existsByEmail( email );
    }


}
