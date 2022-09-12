package com.babaslim.app.service.impl;

import com.babaslim.app.domain.Produit;
import com.babaslim.app.domain.Taille;
import com.babaslim.app.repository.ProduitRepository;
import com.babaslim.app.repository.TailleRepository;
import com.babaslim.app.service.ProduitService;
import com.babaslim.app.service.dto.ProduitDTO;
import com.babaslim.app.service.mapper.ProduitMapper;
import com.babaslim.app.service.mapper.TailleMapper;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Produit}.
 */
@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitServiceImpl.class);

    private final ProduitRepository produitRepository;
    private final TailleRepository tailleRepository;
    private final ProduitMapper produitMapper;
    private final TailleMapper tailleMapper;

    public ProduitServiceImpl(
        ProduitRepository produitRepository,
        TailleRepository tailleRepository,
        ProduitMapper produitMapper,
        TailleMapper taiileMapper
    ) {
        this.produitRepository = produitRepository;
        this.tailleRepository = tailleRepository;
        this.produitMapper = produitMapper;
        this.tailleMapper = taiileMapper;
    }

    @Override
    public ProduitDTO save(ProduitDTO produitDTO) {
        log.debug("Request to save Produit : {}", produitDTO);
        Set<Taille> tailles = produitDTO
            .getTailles()
            .stream()
            .map(taille -> {
                if (taille.getId() != null) {
                    return tailleRepository.findById(taille.getId()).get();
                }
                return tailleRepository.save(tailleMapper.toEntity(taille));
            })
            .collect(Collectors.toSet());
        Produit produit = produitMapper.toEntity(produitDTO);
        produit.setTailles(tailles);
        produit = produitRepository.save(produit);
        return produitMapper.toDto(produit);
    }

    @Override
    public Optional<ProduitDTO> partialUpdate(ProduitDTO produitDTO) {
        log.debug("Request to partially update Produit : {}", produitDTO);
        return produitRepository
            .findById(produitDTO.getId())
            .map(existingProduit -> {
                produitMapper.partialUpdate(existingProduit, produitDTO);

                return existingProduit;
            })
            .map(produitRepository::save)
            .map(produitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProduitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Produits");
        return produitRepository.findAll(pageable).map(produitMapper::toDto);
    }

    public Page<ProduitDTO> findAllWithEagerRelationships(Pageable pageable) {
        return produitRepository.findAllWithEagerRelationships(pageable).map(produitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProduitDTO> findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findOneWithEagerRelationships(id).map(produitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        produitRepository.deleteById(id);
    }
}
