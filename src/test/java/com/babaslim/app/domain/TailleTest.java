package com.babaslim.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TailleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Taille.class);
        Taille taille1 = new Taille();
        taille1.setId(1L);
        Taille taille2 = new Taille();
        taille2.setId(taille1.getId());
        assertThat(taille1).isEqualTo(taille2);
        taille2.setId(2L);
        assertThat(taille1).isNotEqualTo(taille2);
        taille1.setId(null);
        assertThat(taille1).isNotEqualTo(taille2);
    }
}
