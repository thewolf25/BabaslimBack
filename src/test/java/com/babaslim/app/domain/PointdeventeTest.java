package com.babaslim.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointdeventeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pointdevente.class);
        Pointdevente pointdevente1 = new Pointdevente();
        pointdevente1.setId(1L);
        Pointdevente pointdevente2 = new Pointdevente();
        pointdevente2.setId(pointdevente1.getId());
        assertThat(pointdevente1).isEqualTo(pointdevente2);
        pointdevente2.setId(2L);
        assertThat(pointdevente1).isNotEqualTo(pointdevente2);
        pointdevente1.setId(null);
        assertThat(pointdevente1).isNotEqualTo(pointdevente2);
    }
}
