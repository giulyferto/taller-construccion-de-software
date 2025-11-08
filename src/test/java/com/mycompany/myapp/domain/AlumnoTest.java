package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlumnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlumnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alumno.class);
        Alumno alumno1 = getAlumnoSample1();
        Alumno alumno2 = new Alumno();
        assertThat(alumno1).isNotEqualTo(alumno2);

        alumno2.setId(alumno1.getId());
        assertThat(alumno1).isEqualTo(alumno2);

        alumno2 = getAlumnoSample2();
        assertThat(alumno1).isNotEqualTo(alumno2);
    }
}
