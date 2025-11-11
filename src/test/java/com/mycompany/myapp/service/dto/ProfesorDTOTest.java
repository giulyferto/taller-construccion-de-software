package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfesorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfesorDTO.class);
        ProfesorDTO profesorDTO1 = new ProfesorDTO();
        profesorDTO1.setId(1L);
        ProfesorDTO profesorDTO2 = new ProfesorDTO();
        assertThat(profesorDTO1).isNotEqualTo(profesorDTO2);
        profesorDTO2.setId(profesorDTO1.getId());
        assertThat(profesorDTO1).isEqualTo(profesorDTO2);
        profesorDTO2.setId(2L);
        assertThat(profesorDTO1).isNotEqualTo(profesorDTO2);
        profesorDTO1.setId(null);
        assertThat(profesorDTO1).isNotEqualTo(profesorDTO2);
    }
}
