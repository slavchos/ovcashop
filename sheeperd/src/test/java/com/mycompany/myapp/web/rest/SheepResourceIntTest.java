package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Sheep;
import com.mycompany.myapp.repository.SheepRepository;
import com.mycompany.myapp.service.SheepService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SheepResource REST controller.
 *
 * @see SheepResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SheepResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_AGE = 1D;
    private static final Double UPDATED_AGE = 2D;

    private static final Double DEFAULT_AGE_LAST_SHAVED = 1D;
    private static final Double UPDATED_AGE_LAST_SHAVED = 2D;

    @Inject
    private SheepRepository sheepRepository;

    @Inject
    private SheepService sheepService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSheepMockMvc;

    private Sheep sheep;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SheepResource sheepResource = new SheepResource();
        ReflectionTestUtils.setField(sheepResource, "sheepService", sheepService);
        this.restSheepMockMvc = MockMvcBuilders.standaloneSetup(sheepResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sheep = new Sheep();
        sheep.setName(DEFAULT_NAME);
        sheep.setAge(DEFAULT_AGE);
        sheep.setAge_last_shaved(DEFAULT_AGE_LAST_SHAVED);
    }

    @Test
    @Transactional
    public void createSheep() throws Exception {
        int databaseSizeBeforeCreate = sheepRepository.findAll().size();

        // Create the Sheep

        restSheepMockMvc.perform(post("/api/sheeps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sheep)))
                .andExpect(status().isCreated());

        // Validate the Sheep in the database
        List<Sheep> sheeps = sheepRepository.findAll();
        assertThat(sheeps).hasSize(databaseSizeBeforeCreate + 1);
        Sheep testSheep = sheeps.get(sheeps.size() - 1);
        assertThat(testSheep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSheep.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testSheep.getAge_last_shaved()).isEqualTo(DEFAULT_AGE_LAST_SHAVED);
    }

    @Test
    @Transactional
    public void getAllSheeps() throws Exception {
        // Initialize the database
        sheepRepository.saveAndFlush(sheep);

        // Get all the sheeps
        restSheepMockMvc.perform(get("/api/sheeps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sheep.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.doubleValue())))
                .andExpect(jsonPath("$.[*].age_last_shaved").value(hasItem(DEFAULT_AGE_LAST_SHAVED.doubleValue())));
    }

    @Test
    @Transactional
    public void getSheep() throws Exception {
        // Initialize the database
        sheepRepository.saveAndFlush(sheep);

        // Get the sheep
        restSheepMockMvc.perform(get("/api/sheeps/{id}", sheep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sheep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.doubleValue()))
            .andExpect(jsonPath("$.age_last_shaved").value(DEFAULT_AGE_LAST_SHAVED.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSheep() throws Exception {
        // Get the sheep
        restSheepMockMvc.perform(get("/api/sheeps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSheep() throws Exception {
        // Initialize the database
        sheepRepository.saveAndFlush(sheep);

		int databaseSizeBeforeUpdate = sheepRepository.findAll().size();

        // Update the sheep
        sheep.setName(UPDATED_NAME);
        sheep.setAge(UPDATED_AGE);
        sheep.setAge_last_shaved(UPDATED_AGE_LAST_SHAVED);

        restSheepMockMvc.perform(put("/api/sheeps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sheep)))
                .andExpect(status().isOk());

        // Validate the Sheep in the database
        List<Sheep> sheeps = sheepRepository.findAll();
        assertThat(sheeps).hasSize(databaseSizeBeforeUpdate);
        Sheep testSheep = sheeps.get(sheeps.size() - 1);
        assertThat(testSheep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSheep.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSheep.getAge_last_shaved()).isEqualTo(UPDATED_AGE_LAST_SHAVED);
    }

    @Test
    @Transactional
    public void deleteSheep() throws Exception {
        // Initialize the database
        sheepRepository.saveAndFlush(sheep);

		int databaseSizeBeforeDelete = sheepRepository.findAll().size();

        // Get the sheep
        restSheepMockMvc.perform(delete("/api/sheeps/{id}", sheep.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sheep> sheeps = sheepRepository.findAll();
        assertThat(sheeps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
