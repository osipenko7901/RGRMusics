package com.tpp.rgr.controllers;

import com.tpp.rgr.models.Musicgroup;
import com.tpp.rgr.service.MusicgroupService;
import com.tpp.rgr.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/musicgroups")
public class MusicgroupController {

    @Autowired
    private MusicgroupService musicgroupService;

    @Autowired
    private GenreService genreService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listMusicgroups(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("musicgroups", musicgroupService.getAllMusicGroups());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "musicgroups";
    }

    @GetMapping("/add")
    public String addMusicgroupForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("musicgroup", new Musicgroup());
        model.addAttribute("genres", genreService.getAllGenres());
        return "add-musicgroup";
    }

    @PostMapping("/add")
    public String addMusicgroup(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute("musicgroup") Musicgroup musicgroup,
                                BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("genres", genreService.getAllGenres());
            return "add-musicgroup";
        }
        musicgroupService.saveMusicGroup(musicgroup);
        return "redirect:/musicgroups";
    }

    @GetMapping("/edit/{id}")
    public String editMusicgroupForm(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable("id") Integer id,
                                     Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Musicgroup musicgroup = musicgroupService.findMusicGroupById(id).orElse(null);
        if (musicgroup != null) {
            model.addAttribute("musicgroup", musicgroup);
            model.addAttribute("genres", genreService.getAllGenres());
            return "edit-musicgroup";
        } else {
            return "redirect:/musicgroups";
        }
    }

    @PostMapping("/update/{id}")
    public String updateMusicgroup(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("musicgroup") Musicgroup musicgroup,
                                   BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("genres", genreService.getAllGenres());
            return "edit-musicgroup";
        }
        musicgroup.setId(id);
        musicgroupService.updateMusicGroup(musicgroup);
        return "redirect:/musicgroups";
    }

    @GetMapping("/delete/{id}")
    public String deleteMusicgroup(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        musicgroupService.deleteMusicGroup(id);
        return "redirect:/musicgroups";
    }

}
