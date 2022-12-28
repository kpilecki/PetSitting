package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
}
