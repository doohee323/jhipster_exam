package net.slipp.jhipster.service.mapper;

import net.slipp.jhipster.domain.*;
import net.slipp.jhipster.service.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CustomerMapper extends EntityMapper <CustomerDTO, Customer> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CustomerDTO toDto(Customer customer); 

    @Mapping(source = "userId", target = "user")
    Customer toEntity(CustomerDTO customerDTO); 
    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
