package ru.leti.project.models;

import lombok.Data;

@Data
public class Path {
    private String path;

    public Path() {
path = new String();
    }

    public Path(String path) {
        this.path = path;
    }
}
