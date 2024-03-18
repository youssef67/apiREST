package com.example.notemanager;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tabs")
public class Tab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();


    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public List<Note> getNotes() {
        return notes;
    }
}