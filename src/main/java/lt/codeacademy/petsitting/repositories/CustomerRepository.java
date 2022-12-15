package lt.codeacademy.petsitting.repositories;

import lt.codeacademy.petsitting.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Optional<Customer> getCustomersByUsername( String username);
}
