package net.slipp.jhipster.repository;

import net.slipp.jhipster.domain.CartItems;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the CartItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartItemsRepository extends JpaRepository<CartItems,Long> {

    @Query("select cart_items from CartItems cart_items where cart_items.user.login = ?#{principal.username}")
    List<CartItems> findByUserIsCurrentUser();
    
}
