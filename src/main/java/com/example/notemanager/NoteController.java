package com.example.notemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/binder/{idBinder}/tab/{idTab}/note")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TabRepository tabRepository;

    @Autowired
    private BinderRepository binderRepository;

    @GetMapping
    public List<Note> findAllNotes() {
        List<Note> list = (List<Note>) noteRepository.findAll();

        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findNoteById(@PathVariable(value = "id") long id) {
        Optional<Note> note = noteRepository.findById(id);

        if(note.isPresent()) {
            return ResponseEntity.ok().body(note.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveNoteToTabToBinder(@PathVariable long idBinder, @PathVariable long idTab, @Validated @RequestBody Note note) {
        Optional<Binder> binder = binderRepository.findById(idBinder);
        Optional<Tab> tab = tabRepository.findById(idTab);

        if (binder.isPresent() && tab.isPresent()) {
            tab.get().getNotes().add(note);
            noteRepository.save(note);
            return ResponseEntity.ok(note);
        } else {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable(value = "id") long id, @Validated @RequestBody Note noteDetails) {
        Optional<Note> noteOptional = noteRepository.findById(id);

        if(noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setName(noteDetails.getName());

            noteRepository.save(note);
            return ResponseEntity.ok(note);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "idBinder") long idBinder,@PathVariable(value = "idTab") long idTab,  @PathVariable(value = "id") long id) {
        Optional<Binder> binder = binderRepository.findById(idBinder);
        Optional<Tab> tab = tabRepository.findById(idTab);
        Optional<Note> note = noteRepository.findById(id);

        if (binder.isPresent() && tab.isPresent() && note.isPresent()) {
            System.out.println("binder, tab and note are existing");
            boolean belongToBinder = binder.get().getTabs().stream().anyMatch(t -> t.getId().equals(tab.get().getId()));

            if (belongToBinder) {
                System.out.println("belongToBinder");

                boolean belongToTab = tab.get().getNotes().stream().anyMatch(n -> n.getId().equals(note.get().getId()));

                if (belongToTab) {
                    System.out.println("belongToTab");

                    tab.get().getNotes().remove(note.get());
                    noteRepository.delete(note.get());
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}