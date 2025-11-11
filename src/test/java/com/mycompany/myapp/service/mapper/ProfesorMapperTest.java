package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProfesorAsserts.*;
import static com.mycompany.myapp.domain.ProfesorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfesorMapperTest {

    private ProfesorMapper profesorMapper;

    @BeforeEach
    void setUp() {
        profesorMapper = new ProfesorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfesorSample1();
        var actual = profesorMapper.toEntity(profesorMapper.toDto(expected));
        assertProfesorAllPropertiesEquals(expected, actual);
    }
}
