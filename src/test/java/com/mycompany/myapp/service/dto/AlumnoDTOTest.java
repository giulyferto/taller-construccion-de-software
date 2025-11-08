package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlumnoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlumnoDTO.class);
        AlumnoDTO alumnoDTO1 = new AlumnoDTO();
        alumnoDTO1.setId(1L);
        AlumnoDTO alumnoDTO2 = new AlumnoDTO();
        assertThat(alumnoDTO1).isNotEqualTo(alumnoDTO2);
        alumnoDTO2.setId(alumnoDTO1.getId());
        assertThat(alumnoDTO1).isEqualTo(alumnoDTO2);
        alumnoDTO2.setId(2L);
        assertThat(alumnoDTO1).isNotEqualTo(alumnoDTO2);
        alumnoDTO1.setId(null);
        assertThat(alumnoDTO1).isNotEqualTo(alumnoDTO2);
    }
}
