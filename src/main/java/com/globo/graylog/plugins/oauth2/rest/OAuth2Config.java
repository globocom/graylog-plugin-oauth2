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
public abstract class OAuth2Config {

    public static Builder builder() {
        return new AutoValue_OAuth2Config.Builder();
    }

    public abstract Builder toBuilder();

    public static OAuth2Config defaultConfig() {
        return builder()
                .clientId("")
                .clientSecret("")
                .tokenServerUrl("")
                .dataServerUrl("")
                .autoCreateUser(false)
                .useAuthorization(false)
                .defaultGroup("Reader")
                .build();
    }


    @JsonProperty("client_id")
    @Nullable
    public abstract String clientId();

    @JsonProperty("client_secret")
    @Nullable
    public abstract String clientSecret();

    @JsonProperty("token_server_url")
    @Nullable
    public abstract String tokenServerUrl();

    @JsonProperty("data_server_url")
    @Nullable
    public abstract String dataServerUrl();

    @JsonProperty("redirect_url")
    @Nullable
    public abstract String redirectUrl();

    @JsonProperty("auto_create_user")
    public abstract boolean autoCreateUser();

    @JsonProperty("use_authorization")
    public abstract boolean useAuthorization();

    @JsonProperty("default_group")
    @Nullable
    public abstract String defaultGroup();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract OAuth2Config build();

        @JsonProperty("client_id")
        public abstract Builder clientId(@Nullable String clientId);

        @JsonProperty("client_secret")
        public abstract Builder clientSecret(@Nullable String clientSecret);

        @JsonProperty("token_server_url")
        public abstract Builder tokenServerUrl(@Nullable String tokenServerUrl);

        @JsonProperty("data_server_url")
        public abstract Builder dataServerUrl(@Nullable String dataServerUrl);

        @JsonProperty("redirect_url")
        public abstract Builder redirectUrl(@Nullable String redirectUrl);

        @JsonProperty("auto_create_user")
        public abstract Builder autoCreateUser(boolean autoCreateUser);

        @JsonProperty("use_authorization")
        public abstract Builder useAuthorization(boolean useAuthorization);

        @JsonProperty("default_group")
        public abstract Builder defaultGroup(@Nullable String defaultGroup);
    }
}
