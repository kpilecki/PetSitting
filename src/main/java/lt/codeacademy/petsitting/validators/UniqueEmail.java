package lt.codeacademy.petsitting.validators;

import javax.persistence.Table;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint( validatedBy = UniqueEmailValidator.class )
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface UniqueEmail {

    String message() default "Email already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
