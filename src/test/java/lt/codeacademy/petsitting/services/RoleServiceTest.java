package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setup(){
        roleRepository.deleteAll();
    }

    @Test
    void save_whenValidRoleIsPassed_savedRoleIsReturned(){
        Role roleToSave = getValidRole();

        Role savedRole = roleService.save( roleToSave );

        Assertions.assertNotNull( savedRole );
        Assertions.assertEquals( roleToSave.getName(), savedRole.getName() );
    }

    @Test
    void findByName_whenValidRoleExists_roleIsReturned(){
        Role savedRole = roleRepository.save( getValidRole() );

        Optional<Role> optionalRole = roleService.findByName( savedRole.getName() );

        Assertions.assertTrue( optionalRole.isPresent() );
        Assertions.assertEquals( savedRole.getName(), optionalRole.get().getName() );
    }

    @Test
    void findByName_whenNoValidRoleExists_emptyOptionalIsReturned(){
        Assertions.assertEquals( 0, roleRepository.count() );

        Optional<Role> optionalRole = roleService.findByName( UserRoles.ROLE_CUSTOMER );

        Assertions.assertTrue( optionalRole.isEmpty() );
    }

    private Role getValidRole(){
        return new Role( UserRoles.ROLE_CUSTOMER );
    }
}
