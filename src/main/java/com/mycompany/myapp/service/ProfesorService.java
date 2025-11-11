package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Profesor;
import com.mycompany.myapp.repository.ProfesorRepository;
import com.mycompany.myapp.service.dto.ProfesorDTO;
import com.mycompany.myapp.service.mapper.ProfesorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Profesor}.
 */
@Service
@Transactional
public class ProfesorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfesorService.class);

    private final ProfesorRepository profesorRepository;

    private final ProfesorMapper profesorMapper;

    public ProfesorService(ProfesorRepository profesorRepository, ProfesorMapper profesorMapper) {
        this.profesorRepository = profesorRepository;
        this.profesorMapper = profesorMapper;
    }

    /**
     * Save a profesor.
     *
     * @param profesorDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfesorDTO save(ProfesorDTO profesorDTO) {
        LOG.debug("Request to save Profesor : {}", profesorDTO);
        Profesor profesor = profesorMapper.toEntity(profesorDTO);
        profesor = profesorRepository.save(profesor);
        return profesorMapper.toDto(profesor);
    }

    /**
     * Update a profesor.
     *
     * @param profesorDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfesorDTO update(ProfesorDTO profesorDTO) {
        LOG.debug("Request to update Profesor : {}", profesorDTO);
        Profesor profesor = profesorMapper.toEntity(profesorDTO);
        profesor = profesorRepository.save(profesor);
        return profesorMapper.toDto(profesor);
    }

    /**
     * Partially update a profesor.
     *
     * @param profesorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfesorDTO> partialUpdate(ProfesorDTO profesorDTO) {
        LOG.debug("Request to partially update Profesor : {}", profesorDTO);

        return profesorRepository
            .findById(profesorDTO.getId())
            .map(existingProfesor -> {
                profesorMapper.partialUpdate(existingProfesor, profesorDTO);

                return existingProfesor;
            })
            .map(profesorRepository::save)
            .map(profesorMapper::toDto);
    }

    /**
     * Get all the profesors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfesorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Profesors");
        return profesorRepository.findAll(pageable).map(profesorMapper::toDto);
    }

    /**
     * Get one profesor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfesorDTO> findOne(Long id) {
        LOG.debug("Request to get Profesor : {}", id);
        return profesorRepository.findById(id).map(profesorMapper::toDto);
    }

    /**
     * Delete the profesor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Profesor : {}", id);
        profesorRepository.deleteById(id);
    }
}
