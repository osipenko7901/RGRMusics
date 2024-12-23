package com.tpp.rgr.models;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "song_name", nullable = false)
    private String songName;

    @Column(name = "duration", nullable = false)
    private Double duration;

    @Column(name = "genresong", nullable = false)
    private String genreSong;

    @ManyToOne
    @JoinColumn(name = "musicgroup_id", nullable = false)
    private Musicgroup musicGroup;

    // Геттери и сеттери
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    public String getGenreSong() {
        return genreSong;
    }
    public void setGenreSong(String genreSong) {
        this.genreSong = genreSong;
    }
    public Musicgroup getMusicGroup() {
        return musicGroup;
    }
    public void setMusicGroup(Musicgroup musicGroup) {
        this.musicGroup = musicGroup;
    }
}
