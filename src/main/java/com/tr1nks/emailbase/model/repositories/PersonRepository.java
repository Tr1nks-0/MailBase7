package com.tr1nks.emailbase.model.repositories;

import com.tr1nks.emailbase.model.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link JpaRepository JpaRepository} для {@link PersonEntity PersonEntity}
 */
@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
}
