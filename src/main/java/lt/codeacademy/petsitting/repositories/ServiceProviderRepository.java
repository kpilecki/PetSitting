package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
}
