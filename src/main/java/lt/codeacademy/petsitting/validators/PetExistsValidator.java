package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PetExistsValidator implements ConstraintValidator<PetExists, Long> {

    private final PetService petService;

    @Autowired
    public PetExistsValidator( PetService petService ) {
        this.petService = petService;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return petService.findById( value ).isPresent();
    }
}
