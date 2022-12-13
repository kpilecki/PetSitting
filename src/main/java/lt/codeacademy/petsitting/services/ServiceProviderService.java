package lt.codeacademy.petsitting.services;

import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public ServiceProvider save( ServiceProvider serviceProvider ){
        return serviceProviderRepository.save( serviceProvider );
    }
}
