package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CursoTestSamples.*;
import static com.mycompany.myapp.domain.ProfesorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfesorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profesor.class);
        Profesor profesor1 = getProfesorSample1();
        Profesor profesor2 = new Profesor();
        assertThat(profesor1).isNotEqualTo(profesor2);

        profesor2.setId(profesor1.getId());
        assertThat(profesor1).isEqualTo(profesor2);

        profesor2 = getProfesorSample2();
        assertThat(profesor1).isNotEqualTo(profesor2);
    }

    @Test
    void cursoTest() {
        Profesor profesor = getProfesorRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        profesor.addCurso(cursoBack);
        assertThat(profesor.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getProfesor()).isEqualTo(profesor);

        profesor.removeCurso(cursoBack);
        assertThat(profesor.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getProfesor()).isNull();

        profesor.cursos(new HashSet<>(Set.of(cursoBack)));
        assertThat(profesor.getCursos()).containsOnly(cursoBack);
        assertThat(cursoBack.getProfesor()).isEqualTo(profesor);

        profesor.setCursos(new HashSet<>());
        assertThat(profesor.getCursos()).doesNotContain(cursoBack);
        assertThat(cursoBack.getProfesor()).isNull();
    }
}
