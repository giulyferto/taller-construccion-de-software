package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProfesorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Profesor;
import com.mycompany.myapp.repository.ProfesorRepository;
import com.mycompany.myapp.service.dto.ProfesorDTO;
import com.mycompany.myapp.service.mapper.ProfesorMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfesorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfesorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/profesors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProfesorMapper profesorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfesorMockMvc;

    private Profesor profesor;

    private Profesor insertedProfesor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createEntity() {
        return new Profesor().nombre(DEFAULT_NOMBRE).apellido(DEFAULT_APELLIDO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profesor createUpdatedEntity() {
        return new Profesor().nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO);
    }

    @BeforeEach
    void initTest() {
        profesor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfesor != null) {
            profesorRepository.delete(insertedProfesor);
            insertedProfesor = null;
        }
    }

    @Test
    @Transactional
    void createProfesor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);
        var returnedProfesorDTO = om.readValue(
            restProfesorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profesorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfesorDTO.class
        );

        // Validate the Profesor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfesor = profesorMapper.toEntity(returnedProfesorDTO);
        assertProfesorUpdatableFieldsEquals(returnedProfesor, getPersistedProfesor(returnedProfesor));

        insertedProfesor = returnedProfesor;
    }

    @Test
    @Transactional
    void createProfesorWithExistingId() throws Exception {
        // Create the Profesor with an existing ID
        profesor.setId(1L);
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfesorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profesorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profesor.setNombre(null);

        // Create the Profesor, which fails.
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        restProfesorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profesorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfesors() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        // Get all the profesorList
        restProfesorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profesor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)));
    }

    @Test
    @Transactional
    void getProfesor() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        // Get the profesor
        restProfesorMockMvc
            .perform(get(ENTITY_API_URL_ID, profesor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profesor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO));
    }

    @Test
    @Transactional
    void getNonExistingProfesor() throws Exception {
        // Get the profesor
        restProfesorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfesor() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profesor
        Profesor updatedProfesor = profesorRepository.findById(profesor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfesor are not directly saved in db
        em.detach(updatedProfesor);
        updatedProfesor.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO);
        ProfesorDTO profesorDTO = profesorMapper.toDto(updatedProfesor);

        restProfesorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profesorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profesorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfesorToMatchAllProperties(updatedProfesor);
    }

    @Test
    @Transactional
    void putNonExistingProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profesorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profesorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profesorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profesorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor.apellido(UPDATED_APELLIDO);

        restProfesorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfesor))
            )
            .andExpect(status().isOk());

        // Validate the Profesor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfesorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfesor, profesor), getPersistedProfesor(profesor));
    }

    @Test
    @Transactional
    void fullUpdateProfesorWithPatch() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profesor using partial update
        Profesor partialUpdatedProfesor = new Profesor();
        partialUpdatedProfesor.setId(profesor.getId());

        partialUpdatedProfesor.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO);

        restProfesorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfesor))
            )
            .andExpect(status().isOk());

        // Validate the Profesor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfesorUpdatableFieldsEquals(partialUpdatedProfesor, getPersistedProfesor(partialUpdatedProfesor));
    }

    @Test
    @Transactional
    void patchNonExistingProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profesorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profesorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profesorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfesor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profesor.setId(longCount.incrementAndGet());

        // Create the Profesor
        ProfesorDTO profesorDTO = profesorMapper.toDto(profesor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profesorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profesor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfesor() throws Exception {
        // Initialize the database
        insertedProfesor = profesorRepository.saveAndFlush(profesor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profesor
        restProfesorMockMvc
            .perform(delete(ENTITY_API_URL_ID, profesor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profesorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Profesor getPersistedProfesor(Profesor profesor) {
        return profesorRepository.findById(profesor.getId()).orElseThrow();
    }

    protected void assertPersistedProfesorToMatchAllProperties(Profesor expectedProfesor) {
        assertProfesorAllPropertiesEquals(expectedProfesor, getPersistedProfesor(expectedProfesor));
    }

    protected void assertPersistedProfesorToMatchUpdatableProperties(Profesor expectedProfesor) {
        assertProfesorAllUpdatablePropertiesEquals(expectedProfesor, getPersistedProfesor(expectedProfesor));
    }
}
