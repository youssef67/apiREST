package com.example.notemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/binders")
public class BinderController {

    @Autowired
    private BinderRepository binderRepository;

    @GetMapping
    public List<Binder> findAllBinders() {
        List<Binder> list = (List<Binder>) binderRepository.findAll();

        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Binder> findBinderById(@PathVariable(value = "id") long id) {
        Optional<Binder> binder = binderRepository.findById(id);

        if(binder.isPresent()) {
            return ResponseEntity.ok().body(binder.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Binder saveBinder(@Validated @RequestBody Binder binder) {
        return binderRepository.save(binder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBinder(@PathVariable long id) {
        Optional<Binder> binder = binderRepository.findById(id);

        if(!binder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        binderRepository.delete(binder.get());
        return ResponseEntity.ok().build();
    }
}