package com.dteliukov.bookworm.repositories;

import com.dteliukov.bookworm.models.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLocationRepository extends JpaRepository<Location, Integer> {
}
