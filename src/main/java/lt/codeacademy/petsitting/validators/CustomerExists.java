package lt.codeacademy.petsitting.validators;

public @interface CustomerExists {
    String message() default "Customer Not Found";

    Class<?>[] groups() default {};

    Class< ? extends Package>[] payload() default {};
}
