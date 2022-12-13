package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
