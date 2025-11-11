package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Profesor;
import com.mycompany.myapp.service.dto.ProfesorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profesor} and its DTO {@link ProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesorMapper extends EntityMapper<ProfesorDTO, Profesor> {}
