package com.babaslim.app.repository;

import com.babaslim.app.domain.Addresse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Addresse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddresseRepository extends JpaRepository<Addresse, Long> {}
