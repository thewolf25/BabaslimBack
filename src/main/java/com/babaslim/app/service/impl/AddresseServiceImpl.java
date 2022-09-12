package com.babaslim.app.service.impl;

import com.babaslim.app.domain.Addresse;
import com.babaslim.app.repository.AddresseRepository;
import com.babaslim.app.service.AddresseService;
import com.babaslim.app.service.dto.AddresseDTO;
import com.babaslim.app.service.mapper.AddresseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Addresse}.
 */
@Service
@Transactional
public class AddresseServiceImpl implements AddresseService {

    private final Logger log = LoggerFactory.getLogger(AddresseServiceImpl.class);

    private final AddresseRepository addresseRepository;

    private final AddresseMapper addresseMapper;

    public AddresseServiceImpl(AddresseRepository addresseRepository, AddresseMapper addresseMapper) {
        this.addresseRepository = addresseRepository;
        this.addresseMapper = addresseMapper;
    }

    @Override
    public AddresseDTO save(AddresseDTO addresseDTO) {
        log.debug("Request to save Addresse : {}", addresseDTO);
        Addresse addresse = addresseMapper.toEntity(addresseDTO);
        addresse = addresseRepository.save(addresse);
        return addresseMapper.toDto(addresse);
    }

    @Override
    public Optional<AddresseDTO> partialUpdate(AddresseDTO addresseDTO) {
        log.debug("Request to partially update Addresse : {}", addresseDTO);

        return addresseRepository
            .findById(addresseDTO.getId())
            .map(existingAddresse -> {
                addresseMapper.partialUpdate(existingAddresse, addresseDTO);

                return existingAddresse;
            })
            .map(addresseRepository::save)
            .map(addresseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddresseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addresseRepository.findAll(pageable).map(addresseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AddresseDTO> findOne(Long id) {
        log.debug("Request to get Addresse : {}", id);
        return addresseRepository.findById(id).map(addresseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Addresse : {}", id);
        addresseRepository.deleteById(id);
    }
}
