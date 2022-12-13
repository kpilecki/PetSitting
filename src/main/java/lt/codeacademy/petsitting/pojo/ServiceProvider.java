package lt.codeacademy.petsitting.pojo;

import lombok.Builder;

@Builder
public class ServiceProvider extends Customer{

    public ServiceProvider(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
    }
}
