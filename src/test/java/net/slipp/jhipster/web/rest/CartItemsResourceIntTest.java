package net.slipp.jhipster.web.rest;

import net.slipp.jhipster.JhipsterApp;

import net.slipp.jhipster.domain.CartItems;
import net.slipp.jhipster.repository.CartItemsRepository;
import net.slipp.jhipster.repository.search.CartItemsSearchRepository;
import net.slipp.jhipster.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static net.slipp.jhipster.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartItemsResource REST controller.
 *
 * @see CartItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class CartItemsResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATE_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private CartItemsSearchRepository cartItemsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartItemsMockMvc;

    private CartItems cartItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CartItemsResource cartItemsResource = new CartItemsResource(cartItemsRepository, cartItemsSearchRepository);
        this.restCartItemsMockMvc = MockMvcBuilders.standaloneSetup(cartItemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItems createEntity(EntityManager em) {
        CartItems cartItems = new CartItems()
            .createDt(DEFAULT_CREATE_DT);
        return cartItems;
    }

    @Before
    public void initTest() {
        cartItemsSearchRepository.deleteAll();
        cartItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartItems() throws Exception {
        int databaseSizeBeforeCreate = cartItemsRepository.findAll().size();

        // Create the CartItems
        restCartItemsMockMvc.perform(post("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItems)))
            .andExpect(status().isCreated());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeCreate + 1);
        CartItems testCartItems = cartItemsList.get(cartItemsList.size() - 1);
        assertThat(testCartItems.getCreateDt()).isEqualTo(DEFAULT_CREATE_DT);

        // Validate the CartItems in Elasticsearch
        CartItems cartItemsEs = cartItemsSearchRepository.findOne(testCartItems.getId());
        assertThat(cartItemsEs).isEqualToComparingFieldByField(testCartItems);
    }

    @Test
    @Transactional
    public void createCartItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartItemsRepository.findAll().size();

        // Create the CartItems with an existing ID
        cartItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartItemsMockMvc.perform(post("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItems)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);

        // Get all the cartItemsList
        restCartItemsMockMvc.perform(get("/api/cart-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDt").value(hasItem(sameInstant(DEFAULT_CREATE_DT))));
    }

    @Test
    @Transactional
    public void getCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);

        // Get the cartItems
        restCartItemsMockMvc.perform(get("/api/cart-items/{id}", cartItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartItems.getId().intValue()))
            .andExpect(jsonPath("$.createDt").value(sameInstant(DEFAULT_CREATE_DT)));
    }

    @Test
    @Transactional
    public void getNonExistingCartItems() throws Exception {
        // Get the cartItems
        restCartItemsMockMvc.perform(get("/api/cart-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);
        int databaseSizeBeforeUpdate = cartItemsRepository.findAll().size();

        // Update the cartItems
        CartItems updatedCartItems = cartItemsRepository.findOne(cartItems.getId());
        updatedCartItems
            .createDt(UPDATED_CREATE_DT);

        restCartItemsMockMvc.perform(put("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartItems)))
            .andExpect(status().isOk());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeUpdate);
        CartItems testCartItems = cartItemsList.get(cartItemsList.size() - 1);
        assertThat(testCartItems.getCreateDt()).isEqualTo(UPDATED_CREATE_DT);

        // Validate the CartItems in Elasticsearch
        CartItems cartItemsEs = cartItemsSearchRepository.findOne(testCartItems.getId());
        assertThat(cartItemsEs).isEqualToComparingFieldByField(testCartItems);
    }

    @Test
    @Transactional
    public void updateNonExistingCartItems() throws Exception {
        int databaseSizeBeforeUpdate = cartItemsRepository.findAll().size();

        // Create the CartItems

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartItemsMockMvc.perform(put("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItems)))
            .andExpect(status().isCreated());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);
        int databaseSizeBeforeDelete = cartItemsRepository.findAll().size();

        // Get the cartItems
        restCartItemsMockMvc.perform(delete("/api/cart-items/{id}", cartItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cartItemsExistsInEs = cartItemsSearchRepository.exists(cartItems.getId());
        assertThat(cartItemsExistsInEs).isFalse();

        // Validate the database is empty
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);

        // Search the cartItems
        restCartItemsMockMvc.perform(get("/api/_search/cart-items?query=id:" + cartItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDt").value(hasItem(sameInstant(DEFAULT_CREATE_DT))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartItems.class);
        CartItems cartItems1 = new CartItems();
        cartItems1.setId(1L);
        CartItems cartItems2 = new CartItems();
        cartItems2.setId(cartItems1.getId());
        assertThat(cartItems1).isEqualTo(cartItems2);
        cartItems2.setId(2L);
        assertThat(cartItems1).isNotEqualTo(cartItems2);
        cartItems1.setId(null);
        assertThat(cartItems1).isNotEqualTo(cartItems2);
    }
}
