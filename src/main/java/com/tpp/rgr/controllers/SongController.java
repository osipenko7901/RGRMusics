package com.tpp.rgr.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.tpp.rgr.models.Song;
import com.tpp.rgr.service.SongService;
import com.tpp.rgr.service.MusicgroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private MusicgroupService musicgroupService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listSongs(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("songs", songService.getAllSongs());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "songs";
    }

    @GetMapping("/add")
    public String addSongForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("song", new Song());
        model.addAttribute("musicgroups", musicgroupService.getAllMusicGroups());
        return "add-song";
    }

    @PostMapping("/add")
    public String addSong(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @ModelAttribute("song") Song song,
                          BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("musicgroups", musicgroupService.getAllMusicGroups());
            return "add-song";
        }
        songService.saveSong(song);
        return "redirect:/songs";
    }

    @GetMapping("/edit/{id}")
    public String editSongForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Song song = songService.findSongById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found"));

        model.addAttribute("song", song);
        model.addAttribute("musicgroups", musicgroupService.getAllMusicGroups());
        return "edit-song";
    }

    @PostMapping("/update/{id}")
    public String updateSong(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id,
                             @Valid @ModelAttribute("song") Song song,
                             BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("musicgroups", musicgroupService.getAllMusicGroups());
            return "edit-song";
        }
        song.setId(id);
        songService.updateSong(song);
        return "redirect:/songs";
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        songService.deleteSong(id);
        return "redirect:/songs";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
