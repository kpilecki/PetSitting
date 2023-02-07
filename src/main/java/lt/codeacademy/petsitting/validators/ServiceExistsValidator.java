package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceExistsValidator implements ConstraintValidator<ServiceExists, Long> {

    private final ServiceService serviceService;

    @Autowired
    public ServiceExistsValidator( ServiceService serviceService ) {
        this.serviceService = serviceService;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return serviceService.findById( value ).isPresent();
    }
}
