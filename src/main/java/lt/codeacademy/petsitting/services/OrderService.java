package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Order;
import lt.codeacademy.petsitting.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    @Autowired
    public OrderService( OrderRepository orderRepository ) {
        this.orderRepository = orderRepository;
    }

    public Order save( Order order ) {
        return orderRepository.save( order );
    }
}
