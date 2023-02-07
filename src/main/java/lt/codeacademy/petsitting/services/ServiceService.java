package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.Service;
import lt.codeacademy.petsitting.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service save( Service service ) {
        return serviceRepository.save( service );
    }

    public void deleteById( Long id ) {
        serviceRepository.deleteById( id );
    }

    public Optional<Service> findById( Long serviceId ) {
        return serviceRepository.findById( serviceId );
    }
}
