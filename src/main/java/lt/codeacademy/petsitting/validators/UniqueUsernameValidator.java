package lt.codeacademy.petsitting.validators;

import lt.codeacademy.petsitting.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Autowired
    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid( String username, ConstraintValidatorContext context ) {
        return !userService.existsByUsername( username );
    }
}
