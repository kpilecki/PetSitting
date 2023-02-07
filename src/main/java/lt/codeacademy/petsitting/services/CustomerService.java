package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer save( Customer customer ){
        return customerRepository.save( customer );
    }

    public Customer getAuthenticatedCustomer(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return findByUsername( auth.getName() ).orElse( null );
        }
        return null;
    }

    public Optional<Customer> findById( Long customerId ) {
        return customerRepository.findById( customerId );
    }

    public Optional<Customer> findByUsername( String username ) {
        return customerRepository.findByUsername( username );
    }
}
