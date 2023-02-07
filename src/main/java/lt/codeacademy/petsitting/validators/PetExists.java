package lt.codeacademy.petsitting.validators;

public @interface PetExists {
    String message() default "Pet Not Found";

    Class<?>[] groups() default {};

    Class< ? extends Package>[] payload() default {};
}
