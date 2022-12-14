package lt.codeacademy.petsitting.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "roles" )
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Enumerated( EnumType.STRING )
    @Column( length = 30 )
    private UserRoles name;


    public Role( UserRoles role ) {
        this.name = role;
    }
}
