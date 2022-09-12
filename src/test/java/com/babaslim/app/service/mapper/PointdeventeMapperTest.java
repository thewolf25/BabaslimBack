package com.babaslim.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PointdeventeMapperTest {

    private PointdeventeMapper pointdeventeMapper;

    @BeforeEach
    public void setUp() {
        pointdeventeMapper = new PointdeventeMapperImpl();
    }
}
