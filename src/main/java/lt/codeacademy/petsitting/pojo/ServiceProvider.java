package lt.codeacademy.petsitting.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
}
