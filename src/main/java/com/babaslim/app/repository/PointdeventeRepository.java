package com.babaslim.app.repository;

import com.babaslim.app.domain.Pointdevente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pointdevente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointdeventeRepository extends JpaRepository<Pointdevente, Long> {}
