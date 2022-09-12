package com.babaslim.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LignedecommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lignedecommande.class);
        Lignedecommande lignedecommande1 = new Lignedecommande();
        lignedecommande1.setId(1L);
        Lignedecommande lignedecommande2 = new Lignedecommande();
        lignedecommande2.setId(lignedecommande1.getId());
        assertThat(lignedecommande1).isEqualTo(lignedecommande2);
        lignedecommande2.setId(2L);
        assertThat(lignedecommande1).isNotEqualTo(lignedecommande2);
        lignedecommande1.setId(null);
        assertThat(lignedecommande1).isNotEqualTo(lignedecommande2);
    }
}
