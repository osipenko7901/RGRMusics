package com.tpp.rgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgr.models.Musicgroup;

@Repository
public interface MusicgroupRepository extends JpaRepository<Musicgroup, Integer> {

}
