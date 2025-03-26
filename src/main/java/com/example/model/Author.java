package com.example.model;

public class Author {
    private String id;
    private String name;
    private Profile profile;

    public Author() {}

    public Author(String id, String name, Profile profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
}
