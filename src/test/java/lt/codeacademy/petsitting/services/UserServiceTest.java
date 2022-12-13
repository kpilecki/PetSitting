package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.User;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void saveUser_whenValidUserSupplied_userIsSaved(){
        User userToSave = User.builder().username( "John" ).password( "P4ssword" ).build();

        userService.saveUser( userToSave );

        Assertions.assertEquals(1, userRepository.count() );
    }

    @Test
    void saveUser_whenValidUserSupplied_sameUserIsReturned(){
        User userToSave = User.builder().username( "John" ).password( "P4ssword" ).build();

        User savedUser = userService.saveUser( userToSave );

        Assertions.assertEquals( userToSave.getUsername() , savedUser.getUsername() );
        Assertions.assertEquals( userToSave.getPassword(), savedUser.getPassword() );
    }

    @Test
    void saveUser_whenValidUserSupplied_IdIsAssigned(){
        User userToSave = User.builder().username( "John" ).password( "P4ssword" ).build();

        User savedUser = userService.saveUser( userToSave );

        Assertions.assertNotNull( savedUser.getId() );
    }

    @Test
    void getUserById_whenValidUserIsRequested_userIsReturned(){
        User savedUser = userRepository.save( User.builder().username( "John" ).password( "P4ssword" ).build() );

        Optional<User> optionalUser = userService.getUserById( savedUser.getId() );

        Assertions.assertTrue( optionalUser.isPresent() );
    }

    @Test
    void getUserById_whenValidUserIsRequested_correctUserIsReturned(){
        User savedUser = userRepository.save( User.builder().username( "John" ).password( "P4ssword" ).build() );

        Optional<User> optionalUser = userService.getUserById( savedUser.getId() );

        Assertions.assertTrue( optionalUser.isPresent() );
        Assertions.assertEquals( savedUser.getUsername(), optionalUser.get().getUsername() );
    }
}
