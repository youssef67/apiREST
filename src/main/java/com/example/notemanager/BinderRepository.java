package com.example.notemanager;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinderRepository extends CrudRepository<Binder, Long> {
}