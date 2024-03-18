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

    @PutMapping("/{id}")
    public ResponseEntity<Tab> updateTab(@PathVariable(value = "id") long id, @Validated @RequestBody Tab tabDetails) {
        Optional<Tab> tabOptional = tabRepository.findById(id);

        if(tabOptional.isPresent()) {
            Tab tab = tabOptional.get();
            tab.setName(tabDetails.getName());

            tabRepository.save(tab);
            return ResponseEntity.ok(tab);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTab(@PathVariable(value = "idBinder") long idBinder, @PathVariable(value = "id") long id) {
        Optional<Binder> binder = binderRepository.findById(idBinder);
        Optional<Tab> tab = tabRepository.findById(id);

        if (binder.isPresent() && tab.isPresent()) {
            System.out.println("binder and tab are existing");
            boolean belongToBinder = binder.get().getTabs().stream().anyMatch(t -> t.getId().equals(tab.get().getId()));

            if (belongToBinder) {
                System.out.println("belongToBinder");

                binder.get().getTabs().remove(tab.get());
                tabRepository.delete(tab.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}