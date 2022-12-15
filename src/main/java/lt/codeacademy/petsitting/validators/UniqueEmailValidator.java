package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    @Autowired
    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.existsByEmail( email );
    }
}
