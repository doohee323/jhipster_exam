package net.slipp.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.slipp.jhipster.domain.CartItems;

import net.slipp.jhipster.repository.CartItemsRepository;
import net.slipp.jhipster.repository.search.CartItemsSearchRepository;
import net.slipp.jhipster.web.rest.util.HeaderUtil;
import net.slipp.jhipster.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CartItems.
 */
@RestController
@RequestMapping("/api")
public class CartItemsResource {

    private final Logger log = LoggerFactory.getLogger(CartItemsResource.class);

    private static final String ENTITY_NAME = "cartItems";

    private final CartItemsRepository cartItemsRepository;

    private final CartItemsSearchRepository cartItemsSearchRepository;

    public CartItemsResource(CartItemsRepository cartItemsRepository, CartItemsSearchRepository cartItemsSearchRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.cartItemsSearchRepository = cartItemsSearchRepository;
    }

    /**
     * POST  /cart-items : Create a new cartItems.
     *
     * @param cartItems the cartItems to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartItems, or with status 400 (Bad Request) if the cartItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cart-items")
    @Timed
    public ResponseEntity<CartItems> createCartItems(@RequestBody CartItems cartItems) throws URISyntaxException {
        log.debug("REST request to save CartItems : {}", cartItems);
        if (cartItems.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cartItems cannot already have an ID")).body(null);
        }
        CartItems result = cartItemsRepository.save(cartItems);
        cartItemsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cart-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cart-items : Updates an existing cartItems.
     *
     * @param cartItems the cartItems to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartItems,
     * or with status 400 (Bad Request) if the cartItems is not valid,
     * or with status 500 (Internal Server Error) if the cartItems couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cart-items")
    @Timed
    public ResponseEntity<CartItems> updateCartItems(@RequestBody CartItems cartItems) throws URISyntaxException {
        log.debug("REST request to update CartItems : {}", cartItems);
        if (cartItems.getId() == null) {
            return createCartItems(cartItems);
        }
        CartItems result = cartItemsRepository.save(cartItems);
        cartItemsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartItems.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cart-items : get all the cartItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cartItems in body
     */
    @GetMapping("/cart-items")
    @Timed
    public ResponseEntity<List<CartItems>> getAllCartItems(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CartItems");
        Page<CartItems> page = cartItemsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cart-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cart-items : get user's the cartItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cartItems in body
     */
    @GetMapping("/my/cart-items")
    @Timed
    public ResponseEntity<List<CartItems>> getMyCartItems() {
        log.debug("REST request to get a page of MyCartItems");
        List<CartItems> list = cartItemsRepository.findByUserIsCurrentUser();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    /**
     * GET  /cart-items/:id : get the "id" cartItems.
     *
     * @param id the id of the cartItems to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartItems, or with status 404 (Not Found)
     */
    @GetMapping("/cart-items/{id}")
    @Timed
    public ResponseEntity<CartItems> getCartItems(@PathVariable Long id) {
        log.debug("REST request to get CartItems : {}", id);
        CartItems cartItems = cartItemsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartItems));
    }

    /**
     * DELETE  /cart-items/:id : delete the "id" cartItems.
     *
     * @param id the id of the cartItems to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cart-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartItems(@PathVariable Long id) {
        log.debug("REST request to delete CartItems : {}", id);
        cartItemsRepository.delete(id);
        cartItemsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cart-items?query=:query : search for the cartItems corresponding
     * to the query.
     *
     * @param query the query of the cartItems search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cart-items")
    @Timed
    public ResponseEntity<List<CartItems>> searchCartItems(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CartItems for query {}", query);
        Page<CartItems> page = cartItemsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cart-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
