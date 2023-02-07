package lt.codeacademy.petsitting.validators;

public @interface ServiceProviderExists {
    String message() default "Service Provider Not Found";

    Class<?>[] groups() default {};

    Class< ? extends Package>[] payload() default {};
}
