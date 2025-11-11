package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlumnoTestSamples.*;
import static com.mycompany.myapp.domain.CursoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void cursoTest() {
        Alumno alumno = getAlumnoRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        alumno.addCurso(cursoBack);
        assertThat(alumno.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getAlumnos()).containsOnly(alumno);

        alumno.removeCurso(cursoBack);
        assertThat(alumno.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getAlumnos()).doesNotContain(alumno);

        alumno.cursos(new HashSet<>(Set.of(cursoBack)));
        assertThat(alumno.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getAlumnos()).containsOnly(alumno);

        alumno.setCursos(new HashSet<>());
        assertThat(alumno.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getAlumnos()).doesNotContain(alumno);
    }
}
