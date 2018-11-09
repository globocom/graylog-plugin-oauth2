/*
 * This file is part of Graylog Plugin Oauth.
 *
 * Graylog Plugin Oauth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Graylog Plugin Oauth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>
 */

package com.globo.graylog.plugins.oauth2.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class UserBackStage {

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
    private Set<Object> roleIds;

    @JsonProperty("groups")
    private Set<String> groups;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<Object> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Object> roleIds) {
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
