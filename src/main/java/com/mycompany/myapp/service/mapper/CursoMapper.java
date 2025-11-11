package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.domain.Curso;
import com.mycompany.myapp.domain.Profesor;
import com.mycompany.myapp.service.dto.AlumnoDTO;
import com.mycompany.myapp.service.dto.CursoDTO;
import com.mycompany.myapp.service.dto.ProfesorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Curso} and its DTO {@link CursoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CursoMapper extends EntityMapper<CursoDTO, Curso> {
    @Mapping(target = "alumnos", source = "alumnos", qualifiedByName = "alumnoNombreSet")
    @Mapping(target = "profesor", source = "profesor", qualifiedByName = "profesorId")
    CursoDTO toDto(Curso s);

    @Mapping(target = "removeAlumno", ignore = true)
    Curso toEntity(CursoDTO cursoDTO);

    @Named("alumnoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    AlumnoDTO toDtoAlumnoNombre(Alumno alumno);

    @Named("alumnoNombreSet")
    default Set<AlumnoDTO> toDtoAlumnoNombreSet(Set<Alumno> alumno) {
        return alumno.stream().map(this::toDtoAlumnoNombre).collect(Collectors.toSet());
    }

    @Named("profesorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfesorDTO toDtoProfesorId(Profesor profesor);
}
