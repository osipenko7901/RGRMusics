package com.tpp.rgr.controllers;

import com.tpp.rgr.models.Genre;
import com.tpp.rgr.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/genres")  //
public class GenreController {

    @Autowired
    private GenreService genreService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listGenres(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "genres";
    }

    @GetMapping("/add")
    public String addGenreForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("genre", new Genre());
        return "add-genre";
    }

    @PostMapping("/add")
    public String addGenre(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @ModelAttribute("genre") Genre genre,
                           BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-genre";
        }
        genreService.saveGenre(genre);
        return "redirect:/genres";
    }


    @GetMapping("/edit/{id}")
    public String editGenreForm(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable("id") Integer id,
                                Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Genre genre = genreService.findGenreById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
        model.addAttribute("genre", genre);
        return "edit-genre";
    }

    @PostMapping("/update/{id}")
    public String updateGenre(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("id") Integer id,
                              @Valid @ModelAttribute("genre") Genre genre,
                              BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-genre";
        }
        genre.setGenreId(id);
        genreService.updateGenre(genre);
        return "redirect:/genres";
    }

    @GetMapping("/delete/{id}")
    public String deleteGenre(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        genreService.deleteGenre(id);
        return "redirect:/genres";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
