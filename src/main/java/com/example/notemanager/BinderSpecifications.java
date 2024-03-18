package com.example.notemanager;

import org.springframework.data.jpa.domain.Specification;

public class BinderSpecifications {

    public static Specification<Binder> hasNameLike(String name) {
        return (root, query, criteriaBuider) ->
                criteriaBuider.like(root.<String>get("name"), "%" + name + "%");
    }
}