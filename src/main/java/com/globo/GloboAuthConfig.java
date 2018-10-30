package com.globo;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_GloboAuthConfig.Builder.class)
@JsonAutoDetect
public abstract  class GloboAuthConfig {

    public static Builder builder() {
        return new AutoValue_GloboAuthConfig.Builder();
    }

    public abstract Builder toBuilder();

    public static GloboAuthConfig defaultConfig() {
        return builder()
                .name("Graylog")
                .clientId("")
                .clientSecret("")
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

    @JsonProperty("auto_create_user")
    public abstract boolean autoCreateUser();

    @AutoValue.Builder
    public static abstract class Builder {
        abstract GloboAuthConfig build();

        @JsonProperty("name")
        public abstract Builder name(String usernameHeader);

        @JsonProperty("client_id")
        public abstract Builder clientId(@Nullable String fullnameHeader);

        @JsonProperty("client_secret")
        public abstract Builder clientSecret(@Nullable String emailHeader);

        @JsonProperty("auto_create_user")
        public abstract Builder autoCreateUser(boolean autoCreateUser);

    }
}
