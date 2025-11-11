package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Alumno;
import com.mycompany.myapp.domain.Curso;
import com.mycompany.myapp.service.dto.AlumnoDTO;
import com.mycompany.myapp.service.dto.CursoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alumno} and its DTO {@link AlumnoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlumnoMapper extends EntityMapper<AlumnoDTO, Alumno> {
    @Mapping(target = "cursos", source = "cursos", qualifiedByName = "cursoIdSet")
    AlumnoDTO toDto(Alumno s);

    @Mapping(target = "cursos", ignore = true)
    @Mapping(target = "removeCurso", ignore = true)
    Alumno toEntity(AlumnoDTO alumnoDTO);

    @Named("cursoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CursoDTO toDtoCursoId(Curso curso);

    @Named("cursoIdSet")
    default Set<CursoDTO> toDtoCursoIdSet(Set<Curso> curso) {
        return curso.stream().map(this::toDtoCursoId).collect(Collectors.toSet());
    }
}
