package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerExistsValidator implements ConstraintValidator<CustomerExists, String> {

    private final CustomerService customerService;

    @Autowired
    public CustomerExistsValidator( CustomerService customerService ) {
        this.customerService = customerService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return customerService.findByUsername( value ).isPresent();
    }
}
