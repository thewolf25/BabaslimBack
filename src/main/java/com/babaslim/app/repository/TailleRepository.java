package com.babaslim.app.repository;

import com.babaslim.app.domain.Taille;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Taille entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TailleRepository extends JpaRepository<Taille, Long> {}
