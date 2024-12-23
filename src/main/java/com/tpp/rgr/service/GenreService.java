package com.tpp.rgr.service;

import com.tpp.rgr.models.Genre;
import com.tpp.rgr.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    // Получить все жанры
    public List<Genre> getAllGenres() {
        return genreRepository.findAll(Sort.by(Sort.Order.asc("genreId")));
    }

    // Найти жанр по ID
    public Optional<Genre> findGenreById(int id) {
        return genreRepository.findById(id);
    }

    // Сохранить жанр
    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    // Обновить жанр
    public void updateGenre(Genre updatedGenre) {
        Genre existingGenre = genreRepository.findById(updatedGenre.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        existingGenre.setGenreName(updatedGenre.getGenreName());
        existingGenre.setPopularityScore(updatedGenre.getPopularityScore());
        existingGenre.setDescription(updatedGenre.getDescription());

        genreRepository.save(existingGenre);
    }

    // Удалить жанр
    public void deleteGenre(int id) {
        genreRepository.deleteById(id);
    }
}
