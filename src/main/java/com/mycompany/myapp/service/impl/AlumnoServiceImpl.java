package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.repository.AlumnoRepository;
import com.mycompany.myapp.service.AlumnoService;
import com.mycompany.myapp.service.dto.AlumnoDTO;
import com.mycompany.myapp.service.mapper.AlumnoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Alumno}.
 */
@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlumnoServiceImpl.class);

    private final AlumnoRepository alumnoRepository;

    private final AlumnoMapper alumnoMapper;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository, AlumnoMapper alumnoMapper) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoMapper = alumnoMapper;
    }

    @Override
    public AlumnoDTO save(AlumnoDTO alumnoDTO) {
        LOG.debug("Request to save Alumno : {}", alumnoDTO);
        Alumno alumno = alumnoMapper.toEntity(alumnoDTO);
        alumno = alumnoRepository.save(alumno);
        return alumnoMapper.toDto(alumno);
    }

    @Override
    public AlumnoDTO update(AlumnoDTO alumnoDTO) {
        LOG.debug("Request to update Alumno : {}", alumnoDTO);
        Alumno alumno = alumnoMapper.toEntity(alumnoDTO);
        alumno = alumnoRepository.save(alumno);
        return alumnoMapper.toDto(alumno);
    }

    @Override
    public Optional<AlumnoDTO> partialUpdate(AlumnoDTO alumnoDTO) {
        LOG.debug("Request to partially update Alumno : {}", alumnoDTO);

        return alumnoRepository
            .findById(alumnoDTO.getId())
            .map(existingAlumno -> {
                alumnoMapper.partialUpdate(existingAlumno, alumnoDTO);

                return existingAlumno;
            })
            .map(alumnoRepository::save)
            .map(alumnoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Alumnos");
        return alumnoRepository.findAll(pageable).map(alumnoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlumnoDTO> findOne(Long id) {
        LOG.debug("Request to get Alumno : {}", id);
        return alumnoRepository.findById(id).map(alumnoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Alumno : {}", id);
        alumnoRepository.deleteById(id);
    }
}
