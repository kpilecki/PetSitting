package lt.codeacademy.petsitting.validators;

public @interface ServiceExists {
    String message() default "Service Not Found";

    Class<?>[] groups() default {};

    Class< ? extends Package>[] payload() default {};
}
