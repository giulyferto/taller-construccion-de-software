package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AlumnoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Alumno}.
 */
public interface AlumnoService {
    /**
     * Save a alumno.
     *
     * @param alumnoDTO the entity to save.
     * @return the persisted entity.
     */
    AlumnoDTO save(AlumnoDTO alumnoDTO);

    /**
     * Updates a alumno.
     *
     * @param alumnoDTO the entity to update.
     * @return the persisted entity.
     */
    AlumnoDTO update(AlumnoDTO alumnoDTO);

    /**
     * Partially updates a alumno.
     *
     * @param alumnoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlumnoDTO> partialUpdate(AlumnoDTO alumnoDTO);

    /**
     * Get all the alumnos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlumnoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" alumno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlumnoDTO> findOne(Long id);

    /**
     * Delete the "id" alumno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
