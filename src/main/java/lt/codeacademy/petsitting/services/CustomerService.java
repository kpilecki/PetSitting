package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
