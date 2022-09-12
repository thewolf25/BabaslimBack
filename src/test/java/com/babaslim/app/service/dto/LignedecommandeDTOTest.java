package com.babaslim.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LignedecommandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LignedecommandeDTO.class);
        LignedecommandeDTO lignedecommandeDTO1 = new LignedecommandeDTO();
        lignedecommandeDTO1.setId(1L);
        LignedecommandeDTO lignedecommandeDTO2 = new LignedecommandeDTO();
        assertThat(lignedecommandeDTO1).isNotEqualTo(lignedecommandeDTO2);
        lignedecommandeDTO2.setId(lignedecommandeDTO1.getId());
        assertThat(lignedecommandeDTO1).isEqualTo(lignedecommandeDTO2);
        lignedecommandeDTO2.setId(2L);
        assertThat(lignedecommandeDTO1).isNotEqualTo(lignedecommandeDTO2);
        lignedecommandeDTO1.setId(null);
        assertThat(lignedecommandeDTO1).isNotEqualTo(lignedecommandeDTO2);
    }
}
