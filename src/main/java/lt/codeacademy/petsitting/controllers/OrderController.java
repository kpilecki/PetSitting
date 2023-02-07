package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.pojo.*;
import lt.codeacademy.petsitting.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController( OrderService orderService ) {
        this.orderService = orderService;
    }

    @PostMapping( "/new" )
    public ResponseEntity<?> createOrder( @Valid @RequestBody Order request ){
        request.setStatus( OrderStatus.UNCONFIRMED );
        orderService.save( request );
        return ResponseEntity.ok( "Success: Order saved" );
    }


}
