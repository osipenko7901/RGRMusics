package com.tpp.rgr.models;

import jakarta.persistence.*;

@Entity
@Table(name = "musicgroups")
public class Musicgroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "origin_country", nullable = false)
    private String originCountry;

    @Column(name = "foundation_year", nullable = false)
    private Integer foundationYear;

    // Геттери та сеттери
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public String getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
    public Integer getFoundationYear() {
        return foundationYear;
    }
    public void setFoundationYear(Integer foundationYear) {
        this.foundationYear = foundationYear;
    }
}
