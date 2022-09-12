package com.babaslim.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LignedecommandeMapperTest {

    private LignedecommandeMapper lignedecommandeMapper;

    @BeforeEach
    public void setUp() {
        lignedecommandeMapper = new LignedecommandeMapperImpl();
    }
}
