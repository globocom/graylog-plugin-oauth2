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

package com.globo.graylog.plugins.oauth2.service;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_GroupRole.Builder.class)
@JsonAutoDetect
public abstract class GroupRole {

    public static Builder builder() {
        return new AutoValue_GroupRole.Builder();
    }

    public abstract Builder toBuilder();

    public static GroupRole defaultConfig() {
        return builder()
                .group("reader")
                .role("Reader")
                .build();
    }

    @JsonProperty("group")
    @Nullable
    public abstract String group();

    @JsonProperty("role")
    @Nullable
    public abstract String role();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract GroupRole build();

        @JsonProperty("group")
        public abstract Builder group(@Nullable String group);

        @JsonProperty("role")
        public abstract Builder role(@Nullable String role);
    }
}
