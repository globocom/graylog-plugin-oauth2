package com.globo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public class UserBackStage {

    public UserBackStage(){

    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surName;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("role_ids")
    private Set<String> roleIds;

    @JsonProperty("groups")
    private Set<String> groups;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserBackStage{" +
                "name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", picture='" + picture + '\'' +
                ", roleIds=" + roleIds +
                ", groups=" + groups +
                '}';
    }
}
