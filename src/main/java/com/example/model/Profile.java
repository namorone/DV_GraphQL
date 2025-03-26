package com.example.model;

public class Profile {
    private String bio;
    private String website;

    public Profile() {}

    public Profile(String bio, String website) {
        this.bio = bio;
        this.website = website;
    }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}
