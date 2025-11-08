package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.service.dto.AlumnoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alumno} and its DTO {@link AlumnoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlumnoMapper extends EntityMapper<AlumnoDTO, Alumno> {}
