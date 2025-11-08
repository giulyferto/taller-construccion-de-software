package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AlumnoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.domain.enumeration.TipoAlumno;
import com.mycompany.myapp.repository.AlumnoRepository;
import com.mycompany.myapp.service.dto.AlumnoDTO;
import com.mycompany.myapp.service.mapper.AlumnoMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AlumnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlumnoResourceIT {

    private static final Integer DEFAULT_DNI = 1;
    private static final Integer UPDATED_DNI = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final TipoAlumno DEFAULT_TIPO_ALUMNO = TipoAlumno.REGULAR;
    private static final TipoAlumno UPDATED_TIPO_ALUMNO = TipoAlumno.LIBRE;

    private static final BigDecimal DEFAULT_NOTA_PROMEDIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTA_PROMEDIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/alumnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoMapper alumnoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlumnoMockMvc;

    private Alumno alumno;

    private Alumno insertedAlumno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumno createEntity() {
        return new Alumno()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .tipoAlumno(DEFAULT_TIPO_ALUMNO)
            .notaPromedio(DEFAULT_NOTA_PROMEDIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumno createUpdatedEntity() {
        return new Alumno()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .tipoAlumno(UPDATED_TIPO_ALUMNO)
            .notaPromedio(UPDATED_NOTA_PROMEDIO);
    }

    @BeforeEach
    void initTest() {
        alumno = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlumno != null) {
            alumnoRepository.delete(insertedAlumno);
            insertedAlumno = null;
        }
    }

    @Test
    @Transactional
    void createAlumno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);
        var returnedAlumnoDTO = om.readValue(
            restAlumnoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlumnoDTO.class
        );

        // Validate the Alumno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlumno = alumnoMapper.toEntity(returnedAlumnoDTO);
        assertAlumnoUpdatableFieldsEquals(returnedAlumno, getPersistedAlumno(returnedAlumno));

        insertedAlumno = returnedAlumno;
    }

    @Test
    @Transactional
    void createAlumnoWithExistingId() throws Exception {
        // Create the Alumno with an existing ID
        alumno.setId(1L);
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alumno.setDni(null);

        // Create the Alumno, which fails.
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alumno.setNombre(null);

        // Create the Alumno, which fails.
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alumno.setApellido(null);

        // Create the Alumno, which fails.
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alumno.setFechaNacimiento(null);

        // Create the Alumno, which fails.
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlumnos() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        // Get all the alumnoList
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alumno.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].tipoAlumno").value(hasItem(DEFAULT_TIPO_ALUMNO.toString())))
            .andExpect(jsonPath("$.[*].notaPromedio").value(hasItem(sameNumber(DEFAULT_NOTA_PROMEDIO))));
    }

    @Test
    @Transactional
    void getAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        // Get the alumno
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL_ID, alumno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alumno.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.tipoAlumno").value(DEFAULT_TIPO_ALUMNO.toString()))
            .andExpect(jsonPath("$.notaPromedio").value(sameNumber(DEFAULT_NOTA_PROMEDIO)));
    }

    @Test
    @Transactional
    void getNonExistingAlumno() throws Exception {
        // Get the alumno
        restAlumnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno
        Alumno updatedAlumno = alumnoRepository.findById(alumno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlumno are not directly saved in db
        em.detach(updatedAlumno);
        updatedAlumno
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .tipoAlumno(UPDATED_TIPO_ALUMNO)
            .notaPromedio(UPDATED_NOTA_PROMEDIO);
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(updatedAlumno);

        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alumnoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlumnoToMatchAllProperties(updatedAlumno);
    }

    @Test
    @Transactional
    void putNonExistingAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alumnoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alumnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno.fechaNacimiento(UPDATED_FECHA_NACIMIENTO).tipoAlumno(UPDATED_TIPO_ALUMNO).notaPromedio(UPDATED_NOTA_PROMEDIO);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlumnoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlumno, alumno), getPersistedAlumno(alumno));
    }

    @Test
    @Transactional
    void fullUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .tipoAlumno(UPDATED_TIPO_ALUMNO)
            .notaPromedio(UPDATED_NOTA_PROMEDIO);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlumnoUpdatableFieldsEquals(partialUpdatedAlumno, getPersistedAlumno(partialUpdatedAlumno));
    }

    @Test
    @Transactional
    void patchNonExistingAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alumnoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alumnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alumnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlumno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alumno.setId(longCount.incrementAndGet());

        // Create the Alumno
        AlumnoDTO alumnoDTO = alumnoMapper.toDto(alumno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alumnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlumno() throws Exception {
        // Initialize the database
        insertedAlumno = alumnoRepository.saveAndFlush(alumno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alumno
        restAlumnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alumno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alumnoRepository.count();
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

    protected Alumno getPersistedAlumno(Alumno alumno) {
        return alumnoRepository.findById(alumno.getId()).orElseThrow();
    }

    protected void assertPersistedAlumnoToMatchAllProperties(Alumno expectedAlumno) {
        assertAlumnoAllPropertiesEquals(expectedAlumno, getPersistedAlumno(expectedAlumno));
    }

    protected void assertPersistedAlumnoToMatchUpdatableProperties(Alumno expectedAlumno) {
        assertAlumnoAllUpdatablePropertiesEquals(expectedAlumno, getPersistedAlumno(expectedAlumno));
    }
}
