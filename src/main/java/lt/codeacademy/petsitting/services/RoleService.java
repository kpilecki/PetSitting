package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.repositories.RoleRepository;
import lt.codeacademy.petsitting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName( UserRoles name ) {
        return roleRepository.findByName( name );
    }

    public Role save(Role role) {
       return roleRepository.save( role );
    }
}
