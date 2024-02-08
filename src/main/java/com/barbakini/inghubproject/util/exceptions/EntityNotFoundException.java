package com.barbakini.inghubproject.util.exceptions;

import com.barbakini.inghubproject.util.EntityTypeEnum;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String name, EntityTypeEnum type) {
        super(name + " " + type + " is not found.");
    }
}
