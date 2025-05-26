package com.project.code;

/** A Data Transfer Object (DTO) example
 *
 * Does not contain business logic.
 *
 * Its primary purpose is to hold and transfer data.
 */
public class UserRequest {

    private String username;
    private String email;
    private String profileBio;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileBio() {
        return profileBio;
    }

    public void setProfileBio(String profileBio) {
        this.profileBio = profileBio;
    }
}
