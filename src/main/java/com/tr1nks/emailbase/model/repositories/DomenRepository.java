package com.tr1nks.emailbase.model.repositories;


import com.tr1nks.emailbase.model.entities.DomensEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link JpaRepository JpaRepository} для {@link DomensEntity DomensEntity}
 */
@Repository
public interface DomenRepository extends JpaRepository<DomensEntity, Integer> {
    DomensEntity getFirstById(Integer id);

}
