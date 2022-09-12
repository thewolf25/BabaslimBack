package com.babaslim.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TailleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TailleDTO.class);
        TailleDTO tailleDTO1 = new TailleDTO();
        tailleDTO1.setId(1L);
        TailleDTO tailleDTO2 = new TailleDTO();
        assertThat(tailleDTO1).isNotEqualTo(tailleDTO2);
        tailleDTO2.setId(tailleDTO1.getId());
        assertThat(tailleDTO1).isEqualTo(tailleDTO2);
        tailleDTO2.setId(2L);
        assertThat(tailleDTO1).isNotEqualTo(tailleDTO2);
        tailleDTO1.setId(null);
        assertThat(tailleDTO1).isNotEqualTo(tailleDTO2);
    }
}
