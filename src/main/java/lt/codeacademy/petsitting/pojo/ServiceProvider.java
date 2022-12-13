package lt.codeacademy.petsitting.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    public ServiceProvider(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
    }
}
