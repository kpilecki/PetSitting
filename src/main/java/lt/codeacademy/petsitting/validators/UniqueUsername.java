package lt.codeacademy.petsitting.validators;

import javax.persistence.Table;
import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint( validatedBy = UniqueUsernameValidator.class )
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface UniqueUsername {

    String message() default "Username already exists";

    Class<?>[] groups() default {};

    Class< ? extends Package>[] payload() default {};

}
