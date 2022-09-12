package com.babaslim.app.service.impl;

import com.babaslim.app.domain.Lignedecommande;
import com.babaslim.app.repository.LignedecommandeRepository;
import com.babaslim.app.service.LignedecommandeService;
import com.babaslim.app.service.dto.LignedecommandeDTO;
import com.babaslim.app.service.mapper.LignedecommandeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lignedecommande}.
 */
@Service
@Transactional
public class LignedecommandeServiceImpl implements LignedecommandeService {

    private final Logger log = LoggerFactory.getLogger(LignedecommandeServiceImpl.class);

    private final LignedecommandeRepository lignedecommandeRepository;

    private final LignedecommandeMapper lignedecommandeMapper;

    public LignedecommandeServiceImpl(LignedecommandeRepository lignedecommandeRepository, LignedecommandeMapper lignedecommandeMapper) {
        this.lignedecommandeRepository = lignedecommandeRepository;
        this.lignedecommandeMapper = lignedecommandeMapper;
    }

    @Override
    public LignedecommandeDTO save(LignedecommandeDTO lignedecommandeDTO) {
        log.debug("Request to save Lignedecommande : {}", lignedecommandeDTO);
        Lignedecommande lignedecommande = lignedecommandeMapper.toEntity(lignedecommandeDTO);
        lignedecommande = lignedecommandeRepository.save(lignedecommande);
        return lignedecommandeMapper.toDto(lignedecommande);
    }

    @Override
    public Optional<LignedecommandeDTO> partialUpdate(LignedecommandeDTO lignedecommandeDTO) {
        log.debug("Request to partially update Lignedecommande : {}", lignedecommandeDTO);

        return lignedecommandeRepository
            .findById(lignedecommandeDTO.getId())
            .map(existingLignedecommande -> {
                lignedecommandeMapper.partialUpdate(existingLignedecommande, lignedecommandeDTO);

                return existingLignedecommande;
            })
            .map(lignedecommandeRepository::save)
            .map(lignedecommandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LignedecommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lignedecommandes");
        return lignedecommandeRepository.findAll(pageable).map(lignedecommandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LignedecommandeDTO> findOne(Long id) {
        log.debug("Request to get Lignedecommande : {}", id);
        return lignedecommandeRepository.findById(id).map(lignedecommandeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lignedecommande : {}", id);
        lignedecommandeRepository.deleteById(id);
    }
}
