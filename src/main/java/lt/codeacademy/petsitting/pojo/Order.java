package lt.codeacademy.petsitting.pojo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table( name = "orders" )
public class Order {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @ManyToOne
    private ServiceProvider provider;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Service service;

    @ManyToOne
    private Pet pet;

    private LocalDate date;

    private boolean confirmed;

}
