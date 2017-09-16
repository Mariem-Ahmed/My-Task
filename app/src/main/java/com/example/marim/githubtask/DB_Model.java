package com.example.marim.githubtask;

/**
 * Created by marim on 8/21/2017.
 */

public class DB_Model {

    private int ID;
    private String name;
    private String description;
    private String username;
    private String fork;
    private String repo_url;
    private String owner_url;


    public DB_Model() {
    }

    public DB_Model(int ID, String name, String description, String username, String fork, String repo_url, String owner_url) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.username = username;
        this.fork = fork;
        this.repo_url = repo_url;
        this.owner_url = owner_url;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFork() {
        return fork;
    }

    public void setFork(String fork) {
        this.fork = fork;
    }

    public String getRepo_url() {
        return repo_url;
    }

    public void setRepo_url(String repo_url) {
        this.repo_url = repo_url;
    }

    public String getOwner_url() {
        return owner_url;
    }

    public void setOwner_url(String owner_url) {
        this.owner_url = owner_url;
    }

    @Override
    public String toString() {
        return "DB_Model{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", fork='" + fork + '\'' +
                ", repo_url='" + repo_url + '\'' +
                ", owner_url='" + owner_url + '\'' +
                '}';
    }
}