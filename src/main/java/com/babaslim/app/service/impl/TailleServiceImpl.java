package com.babaslim.app.service.impl;

import com.babaslim.app.domain.Taille;
import com.babaslim.app.repository.TailleRepository;
import com.babaslim.app.service.TailleService;
import com.babaslim.app.service.dto.TailleDTO;
import com.babaslim.app.service.mapper.TailleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Taille}.
 */
@Service
@Transactional
public class TailleServiceImpl implements TailleService {

    private final Logger log = LoggerFactory.getLogger(TailleServiceImpl.class);

    private final TailleRepository tailleRepository;

    private final TailleMapper tailleMapper;

    public TailleServiceImpl(TailleRepository tailleRepository, TailleMapper tailleMapper) {
        this.tailleRepository = tailleRepository;
        this.tailleMapper = tailleMapper;
    }

    @Override
    public TailleDTO save(TailleDTO tailleDTO) {
        log.debug("Request to save Taille : {}", tailleDTO);
        Taille taille = tailleMapper.toEntity(tailleDTO);
        taille = tailleRepository.save(taille);
        return tailleMapper.toDto(taille);
    }

    @Override
    public Optional<TailleDTO> partialUpdate(TailleDTO tailleDTO) {
        log.debug("Request to partially update Taille : {}", tailleDTO);

        return tailleRepository
            .findById(tailleDTO.getId())
            .map(existingTaille -> {
                tailleMapper.partialUpdate(existingTaille, tailleDTO);

                return existingTaille;
            })
            .map(tailleRepository::save)
            .map(tailleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TailleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tailles");
        return tailleRepository.findAll(pageable).map(tailleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TailleDTO> findOne(Long id) {
        log.debug("Request to get Taille : {}", id);
        return tailleRepository.findById(id).map(tailleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Taille : {}", id);
        tailleRepository.deleteById(id);
    }
}
