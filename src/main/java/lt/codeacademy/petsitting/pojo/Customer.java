package lt.codeacademy.petsitting.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class Customer extends User{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )

    private String firstName;
    private String lastName;

}
