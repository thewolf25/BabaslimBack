package com.babaslim.app.service.mapper;

import com.babaslim.app.domain.Stock;
import com.babaslim.app.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockMapper extends EntityMapper<StockDTO, Stock> {}
