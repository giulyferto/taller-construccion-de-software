package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AlumnoAsserts.*;
import static com.mycompany.myapp.domain.AlumnoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlumnoMapperTest {

    private AlumnoMapper alumnoMapper;

    @BeforeEach
    void setUp() {
        alumnoMapper = new AlumnoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlumnoSample1();
        var actual = alumnoMapper.toEntity(alumnoMapper.toDto(expected));
        assertAlumnoAllPropertiesEquals(expected, actual);
    }
}
