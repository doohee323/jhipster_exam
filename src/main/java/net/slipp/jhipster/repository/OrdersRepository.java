package net.slipp.jhipster.repository;

import net.slipp.jhipster.domain.Orders;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Orders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {

    @Query("select orders from Orders orders where orders.user.login = ?#{principal.username}")
    List<Orders> findByUserIsCurrentUser();
    
}
