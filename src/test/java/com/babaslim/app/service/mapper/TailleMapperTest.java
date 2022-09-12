package com.babaslim.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TailleMapperTest {

    private TailleMapper tailleMapper;

    @BeforeEach
    public void setUp() {
        tailleMapper = new TailleMapperImpl();
    }
}
