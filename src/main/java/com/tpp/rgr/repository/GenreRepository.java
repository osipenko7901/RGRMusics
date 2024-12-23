package com.tpp.rgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgr.models.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
