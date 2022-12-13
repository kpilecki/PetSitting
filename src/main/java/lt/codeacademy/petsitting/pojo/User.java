package lt.codeacademy.petsitting.pojo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    private String username;
    private String password;
    private String email;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn( name = "user_id" ),
                inverseJoinColumns = @JoinColumn( name = "role_id" ))
    private Set<Role> roles = new HashSet<>();


}
