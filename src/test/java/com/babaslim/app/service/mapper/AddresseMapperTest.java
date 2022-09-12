package com.babaslim.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddresseMapperTest {

    private AddresseMapper addresseMapper;

    @BeforeEach
    public void setUp() {
        addresseMapper = new AddresseMapperImpl();
    }
}
