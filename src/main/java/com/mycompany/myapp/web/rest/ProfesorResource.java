package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProfesorRepository;
import com.mycompany.myapp.service.ProfesorService;
import com.mycompany.myapp.service.dto.ProfesorDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Profesor}.
 */
@RestController
@RequestMapping("/api/profesors")
public class ProfesorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfesorResource.class);

    private static final String ENTITY_NAME = "profesor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfesorService profesorService;

    private final ProfesorRepository profesorRepository;

    public ProfesorResource(ProfesorService profesorService, ProfesorRepository profesorRepository) {
        this.profesorService = profesorService;
        this.profesorRepository = profesorRepository;
    }

    /**
     * {@code POST  /profesors} : Create a new profesor.
     *
     * @param profesorDTO the profesorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profesorDTO, or with status {@code 400 (Bad Request)} if the profesor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfesorDTO> createProfesor(@Valid @RequestBody ProfesorDTO profesorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Profesor : {}", profesorDTO);
        if (profesorDTO.getId() != null) {
            throw new BadRequestAlertException("A new profesor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profesorDTO = profesorService.save(profesorDTO);
        return ResponseEntity.created(new URI("/api/profesors/" + profesorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, profesorDTO.getId().toString()))
            .body(profesorDTO);
    }

    /**
     * {@code PUT  /profesors/:id} : Updates an existing profesor.
     *
     * @param id the id of the profesorDTO to save.
     * @param profesorDTO the profesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profesorDTO,
     * or with status {@code 400 (Bad Request)} if the profesorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> updateProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfesorDTO profesorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Profesor : {}, {}", id, profesorDTO);
        if (profesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profesorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profesorDTO = profesorService.update(profesorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profesorDTO.getId().toString()))
            .body(profesorDTO);
    }

    /**
     * {@code PATCH  /profesors/:id} : Partial updates given fields of an existing profesor, field will ignore if it is null
     *
     * @param id the id of the profesorDTO to save.
     * @param profesorDTO the profesorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profesorDTO,
     * or with status {@code 400 (Bad Request)} if the profesorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profesorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profesorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfesorDTO> partialUpdateProfesor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfesorDTO profesorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Profesor partially : {}, {}", id, profesorDTO);
        if (profesorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profesorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profesorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfesorDTO> result = profesorService.partialUpdate(profesorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profesorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /profesors} : get all the profesors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profesors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfesorDTO>> getAllProfesors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Profesors");
        Page<ProfesorDTO> page = profesorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profesors/:id} : get the "id" profesor.
     *
     * @param id the id of the profesorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profesorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> getProfesor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Profesor : {}", id);
        Optional<ProfesorDTO> profesorDTO = profesorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profesorDTO);
    }

    /**
     * {@code DELETE  /profesors/:id} : delete the "id" profesor.
     *
     * @param id the id of the profesorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Profesor : {}", id);
        profesorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
