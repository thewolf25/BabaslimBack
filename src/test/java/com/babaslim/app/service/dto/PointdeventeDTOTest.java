package com.babaslim.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.babaslim.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointdeventeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointdeventeDTO.class);
        PointdeventeDTO pointdeventeDTO1 = new PointdeventeDTO();
        pointdeventeDTO1.setId(1L);
        PointdeventeDTO pointdeventeDTO2 = new PointdeventeDTO();
        assertThat(pointdeventeDTO1).isNotEqualTo(pointdeventeDTO2);
        pointdeventeDTO2.setId(pointdeventeDTO1.getId());
        assertThat(pointdeventeDTO1).isEqualTo(pointdeventeDTO2);
        pointdeventeDTO2.setId(2L);
        assertThat(pointdeventeDTO1).isNotEqualTo(pointdeventeDTO2);
        pointdeventeDTO1.setId(null);
        assertThat(pointdeventeDTO1).isNotEqualTo(pointdeventeDTO2);
    }
}
