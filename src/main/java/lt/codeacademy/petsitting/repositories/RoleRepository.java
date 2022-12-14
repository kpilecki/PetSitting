package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName( UserRoles name );
}
