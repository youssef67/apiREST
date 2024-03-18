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

    @PutMapping("/{id}")
    public ResponseEntity<Binder> updateBinder(@PathVariable(value = "id") long id, @Validated @RequestBody Binder binderDetails) {
        Optional<Binder> binderOptional = binderRepository.findById(id);

        if(binderOptional.isPresent()) {
            Binder binder = binderOptional.get();
            binder.setName(binderDetails.getName());

            binderRepository.save(binder);
            return ResponseEntity.ok(binder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBinder(@PathVariable(value = "id") long id) {
        Optional<Binder> binder = binderRepository.findById(id);

        if(binder.isPresent()) {
            binderRepository.delete(binder.get());
            return ResponseEntity.ok().body(binder.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}