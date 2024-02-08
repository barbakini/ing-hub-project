package com.barbakini.inghubproject.util.exceptions;

import com.barbakini.inghubproject.util.EntityTypeEnum;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(String name, EntityTypeEnum type) {
        super(name + " " + type + " is already exist.");
    }
}
