package com.babaslim.app.service.impl;

import com.babaslim.app.domain.Pointdevente;
import com.babaslim.app.repository.PointdeventeRepository;
import com.babaslim.app.service.PointdeventeService;
import com.babaslim.app.service.dto.PointdeventeDTO;
import com.babaslim.app.service.mapper.PointdeventeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pointdevente}.
 */
@Service
@Transactional
public class PointdeventeServiceImpl implements PointdeventeService {

    private final Logger log = LoggerFactory.getLogger(PointdeventeServiceImpl.class);

    private final PointdeventeRepository pointdeventeRepository;

    private final PointdeventeMapper pointdeventeMapper;

    public PointdeventeServiceImpl(PointdeventeRepository pointdeventeRepository, PointdeventeMapper pointdeventeMapper) {
        this.pointdeventeRepository = pointdeventeRepository;
        this.pointdeventeMapper = pointdeventeMapper;
    }

    @Override
    public PointdeventeDTO save(PointdeventeDTO pointdeventeDTO) {
        log.debug("Request to save Pointdevente : {}", pointdeventeDTO);
        Pointdevente pointdevente = pointdeventeMapper.toEntity(pointdeventeDTO);
        pointdevente = pointdeventeRepository.save(pointdevente);
        return pointdeventeMapper.toDto(pointdevente);
    }

    @Override
    public Optional<PointdeventeDTO> partialUpdate(PointdeventeDTO pointdeventeDTO) {
        log.debug("Request to partially update Pointdevente : {}", pointdeventeDTO);

        return pointdeventeRepository
            .findById(pointdeventeDTO.getId())
            .map(existingPointdevente -> {
                pointdeventeMapper.partialUpdate(existingPointdevente, pointdeventeDTO);

                return existingPointdevente;
            })
            .map(pointdeventeRepository::save)
            .map(pointdeventeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PointdeventeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pointdeventes");
        return pointdeventeRepository.findAll(pageable).map(pointdeventeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PointdeventeDTO> findOne(Long id) {
        log.debug("Request to get Pointdevente : {}", id);
        return pointdeventeRepository.findById(id).map(pointdeventeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pointdevente : {}", id);
        pointdeventeRepository.deleteById(id);
    }
}
