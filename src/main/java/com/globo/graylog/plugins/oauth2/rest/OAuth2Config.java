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

package com.globo.graylog.plugins.oauth2.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_OAuth2Config.Builder.class)
@JsonAutoDetect
public abstract  class OAuth2Config {

    public static Builder builder() {
        return new AutoValue_OAuth2Config.Builder();
    }

    public abstract Builder toBuilder();

    public static OAuth2Config defaultConfig() {
        return builder()
                .name("Graylog")
                .clientId("")
                .clientSecret("")
                .urlBackstage("")
                .urlRedirect("")
                .autoCreateUser(false)
                .build();
    }

    @JsonProperty("name")
    public abstract String name();

    @JsonProperty("client_id")
    @Nullable
    public abstract String clientId();

    @JsonProperty("client_secret")
    @Nullable
    public abstract String clientSecret();

    @JsonProperty("url_backstage")
    @Nullable
    public abstract String urlBackstage();

    @JsonProperty("auto_create_user")
    public abstract boolean autoCreateUser();

    @JsonProperty("url_redirect")
    public abstract String urlRedirect();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract OAuth2Config build();

        @JsonProperty("name")
        public abstract Builder name(String name);

        @JsonProperty("client_id")
        public abstract Builder clientId(@Nullable String clientId);

        @JsonProperty("client_secret")
        public abstract Builder clientSecret(@Nullable String clientSecret);

        @JsonProperty("url_backstage")
        public abstract Builder urlBackstage(@Nullable String urlBackstage);

        @JsonProperty("url_redirect")
        public abstract Builder urlRedirect(@Nullable String urlRedirect);

        @JsonProperty("auto_create_user")
        public abstract Builder autoCreateUser(boolean autoCreateUser);

    }
}
