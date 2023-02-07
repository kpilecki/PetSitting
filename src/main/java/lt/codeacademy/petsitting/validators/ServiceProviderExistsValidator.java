package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceProviderExistsValidator implements ConstraintValidator<ServiceProviderExists, String> {

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ServiceProviderExistsValidator( ServiceProviderService serviceProviderService ) {
        this.serviceProviderService = serviceProviderService;
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        return serviceProviderService.findByUsername( value ).isPresent();
    }
}
