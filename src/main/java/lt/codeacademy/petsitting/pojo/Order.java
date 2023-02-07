package lt.codeacademy.petsitting.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.codeacademy.petsitting.validators.CustomerExists;
import lt.codeacademy.petsitting.validators.PetExists;
import lt.codeacademy.petsitting.validators.ServiceExists;
import lt.codeacademy.petsitting.validators.ServiceProviderExists;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "orders" )
public class Order {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @ServiceProviderExists
    @NotBlank
    private String serviceProviderUsername;

    @CustomerExists
    @NotBlank
    private String customerUsername;

    @ServiceExists
    @NotNull
    private Long serviceId;

    @PetExists
    @NotNull
    private Long petId;

    @NotNull
    private LocalDate date;

    @Enumerated( EnumType.STRING )
    private OrderStatus status;

}
