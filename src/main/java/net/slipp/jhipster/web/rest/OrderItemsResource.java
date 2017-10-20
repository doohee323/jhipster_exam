package net.slipp.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.slipp.jhipster.domain.OrderItems;

import net.slipp.jhipster.repository.OrderItemsRepository;
import net.slipp.jhipster.repository.search.OrderItemsSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OrderItems.
 */
@RestController
@RequestMapping("/api")
public class OrderItemsResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemsResource.class);

    private static final String ENTITY_NAME = "orderItems";

    private final OrderItemsRepository orderItemsRepository;

    private final OrderItemsSearchRepository orderItemsSearchRepository;

    public OrderItemsResource(OrderItemsRepository orderItemsRepository, OrderItemsSearchRepository orderItemsSearchRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemsSearchRepository = orderItemsSearchRepository;
    }

    /**
     * POST  /order-items : Create a new orderItems.
     *
     * @param orderItems the orderItems to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderItems, or with status 400 (Bad Request) if the orderItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-items")
    @Timed
    public ResponseEntity<OrderItems> createOrderItems(@Valid @RequestBody OrderItems orderItems) throws URISyntaxException {
        log.debug("REST request to save OrderItems : {}", orderItems);
        if (orderItems.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new orderItems cannot already have an ID")).body(null);
        }
        OrderItems result = orderItemsRepository.save(orderItems);
        orderItemsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-items : Updates an existing orderItems.
     *
     * @param orderItems the orderItems to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderItems,
     * or with status 400 (Bad Request) if the orderItems is not valid,
     * or with status 500 (Internal Server Error) if the orderItems couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-items")
    @Timed
    public ResponseEntity<OrderItems> updateOrderItems(@Valid @RequestBody OrderItems orderItems) throws URISyntaxException {
        log.debug("REST request to update OrderItems : {}", orderItems);
        if (orderItems.getId() == null) {
            return createOrderItems(orderItems);
        }
        OrderItems result = orderItemsRepository.save(orderItems);
        orderItemsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderItems.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-items : get all the orderItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderItems in body
     */
    @GetMapping("/order-items")
    @Timed
    public ResponseEntity<List<OrderItems>> getAllOrderItems(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OrderItems");
        Page<OrderItems> page = orderItemsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-items/:id : get the "id" orderItems.
     *
     * @param id the id of the orderItems to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderItems, or with status 404 (Not Found)
     */
    @GetMapping("/order-items/{id}")
    @Timed
    public ResponseEntity<OrderItems> getOrderItems(@PathVariable Long id) {
        log.debug("REST request to get OrderItems : {}", id);
        OrderItems orderItems = orderItemsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(orderItems));
    }

    /**
     * DELETE  /order-items/:id : delete the "id" orderItems.
     *
     * @param id the id of the orderItems to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderItems(@PathVariable Long id) {
        log.debug("REST request to delete OrderItems : {}", id);
        orderItemsRepository.delete(id);
        orderItemsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-items?query=:query : search for the orderItems corresponding
     * to the query.
     *
     * @param query the query of the orderItems search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-items")
    @Timed
    public ResponseEntity<List<OrderItems>> searchOrderItems(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of OrderItems for query {}", query);
        Page<OrderItems> page = orderItemsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
