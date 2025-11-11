package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlumnoTestSamples.*;
import static com.mycompany.myapp.domain.CursoTestSamples.*;
import static com.mycompany.myapp.domain.ProfesorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CursoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Curso.class);
        Curso curso1 = getCursoSample1();
        Curso curso2 = new Curso();
        assertThat(curso1).isNotEqualTo(curso2);

        curso2.setId(curso1.getId());
        assertThat(curso1).isEqualTo(curso2);

        curso2 = getCursoSample2();
        assertThat(curso1).isNotEqualTo(curso2);
    }

    @Test
    void alumnoTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Alumno alumnoBack = getAlumnoRandomSampleGenerator();

        curso.addAlumno(alumnoBack);
        assertThat(curso.getAlumnos()).containsOnly(alumnoBack);

        curso.removeAlumno(alumnoBack);
        assertThat(curso.getAlumnos()).doesNotContain(alumnoBack);

        curso.alumnos(new HashSet<>(Set.of(alumnoBack)));
        assertThat(curso.getAlumnos()).containsOnly(alumnoBack);

        curso.setAlumnos(new HashSet<>());
        assertThat(curso.getAlumnos()).doesNotContain(alumnoBack);
    }

    @Test
    void profesorTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Profesor profesorBack = getProfesorRandomSampleGenerator();

        curso.setProfesor(profesorBack);
        assertThat(curso.getProfesor()).isEqualTo(profesorBack);

        curso.profesor(null);
        assertThat(curso.getProfesor()).isNull();
    }
}
