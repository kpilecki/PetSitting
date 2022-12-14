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
        User userToSave = createValidUser();

        userService.saveUser( userToSave );

        Assertions.assertEquals(1, userRepository.count() );
    }

    @Test
    void saveUser_whenValidUserSupplied_sameUserIsReturned(){
        User userToSave = createValidUser();

        User savedUser = userService.saveUser( userToSave );

        Assertions.assertEquals( userToSave.getUsername() , savedUser.getUsername() );
        Assertions.assertEquals( userToSave.getPassword(), savedUser.getPassword() );
    }

    @Test
    void saveUser_whenValidUserSupplied_IdIsAssigned(){
        User userToSave = createValidUser();

        User savedUser = userService.saveUser( userToSave );

        Assertions.assertNotNull( savedUser.getId() );
    }

    @Test
    void getUserById_whenValidUserIsRequested_userIsReturned(){
        User savedUser = userRepository.save( createValidUser() );

        Optional<User> optionalUser = userService.getUserById( savedUser.getId() );

        Assertions.assertTrue( optionalUser.isPresent() );
    }

    @Test
    void getUserById_whenValidUserIsRequested_correctUserIsReturned(){
        User savedUser = userRepository.save( createValidUser() );

        Optional<User> optionalUser = userService.getUserById( savedUser.getId() );

        Assertions.assertTrue( optionalUser.isPresent() );
        Assertions.assertEquals( savedUser.getUsername(), optionalUser.get().getUsername() );
    }

    @Test
    void findByUsername_whenValidUserExists_userIsReturned(){
        User savedUser = userRepository.save( createValidUser() );

        Optional<User> optionalUser = userService.findByUsername( savedUser.getUsername() );

        Assertions.assertTrue( optionalUser.isPresent() );
        Assertions.assertEquals( savedUser.getId(), optionalUser.get().getId() );
    }

    @Test
    void findByUsername_whenUserNotFound_emptyOptionalIsReturned(){
        Assertions.assertEquals( 0, userRepository.count() );

        Optional<User> optionalUser = userService.findByUsername( "username" );

        Assertions.assertTrue( optionalUser.isEmpty() );
    }

    @Test
    void existsByUsername_whenValidUserExists_trueIsReturned(){
        User savedUser = userRepository.save( createValidUser() );

        boolean doesUserExist = userService.existsByUsername( savedUser.getUsername() );

        Assertions.assertTrue( doesUserExist );
    }

    @Test
    void existsByUsername_whenNoValidUserExist_falseIsReturned(){
        Assertions.assertEquals( 0, userRepository.count() );

        boolean doesUserExist = userService.existsByUsername( "username" );

        Assertions.assertFalse( doesUserExist );
    }

    @Test
    void existsByEmail_whenValidUserExists_trueIsReturned(){
        User savedUser = userRepository.save( createValidUser() );

        boolean doesUserExist = userService.existsByEmail( savedUser.getEmail() );

        Assertions.assertTrue( doesUserExist );
    }

    @Test
    void existsByEmail_whenNoValidUserExist_falseIsReturned(){
        Assertions.assertEquals( 0, userRepository.count() );

        boolean doesUserExist = userService.existsByUsername( "email@email.com" );

        Assertions.assertFalse( doesUserExist );
    }

    private User createValidUser(){
        return User
                .builder()
                .username( "John" )
                .password( "P4ssword" )
                .email( "email@provider.com" )
                .build();
    }
}
