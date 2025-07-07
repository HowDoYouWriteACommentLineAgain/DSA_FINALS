package org.dsa.models.objects;

import org.dsa.abstractions.objectModel;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private boolean isLoggedIn;

    public User()
    {
        this.id = 1;
        this.username = "test";
        this.passwordHash = "ing";
        this.isLoggedIn = true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

}

