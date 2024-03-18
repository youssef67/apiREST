package com.example.notemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/binder/{idBinder}/tab")
public class TabsController {

    @Autowired
    private TabRepository tabRepository;

    @Autowired
    private BinderRepository binderRepository;

    @GetMapping
    public List<Tab> findAllTabs() {
        List<Tab> list = (List<Tab>) tabRepository.findAll();

        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tab> findTabById(@PathVariable(value = "id") long id) {
        Optional<Tab> tab = tabRepository.findById(id);

        if(tab.isPresent()) {
            return ResponseEntity.ok().body(tab.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveTabToBinder(@PathVariable long idBinder, @Validated @RequestBody Tab tab) {
        return binderRepository.findById(idBinder).map( binder -> {
            binder.getTabs().add(tab);
            tabRepository.save(tab);
            return ResponseEntity.ok(tab);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTab(@PathVariable long id) {
        Optional<Tab> tab = tabRepository.findById(id);

        if(!tab.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        tabRepository.delete(tab.get());
        return ResponseEntity.ok().build();
    }
}