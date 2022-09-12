package com.babaslim.app.repository;

import com.babaslim.app.domain.Lignedecommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lignedecommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LignedecommandeRepository extends JpaRepository<Lignedecommande, Long> {}
