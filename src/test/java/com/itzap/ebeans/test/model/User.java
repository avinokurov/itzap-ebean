package com.itzap.ebeans.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = User.Builder.class)
public class User extends Auditable {
    private final String lastName;
    private final String firstName;
    private final String email;
    private final String phone;
    private final String accountname;
    private final List<String> authorities;

    @JsonProperty("enabled")
    private final Boolean enabled;
    private final String username;

    @JsonIgnore
    private final String password;

    public User(Builder builder) {
        super(builder);

        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.enabled = builder.enabled;
        this.username = builder.username;
        this.accountname = builder.accountname;
        this.password = builder.password;
        this.authorities = builder.authorities;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("lastName", lastName)
                .add("email", email)
                .add("username", username)
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "set")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder extends Auditable.Builder<User, User.Builder> {
        private String lastName;
        private String firstName;
        private String email;
        private String phone;
        private Boolean enabled;
        private String username;
        private String accountname;
        private String password;
        private List<String> authorities = new ArrayList<>();
        String callbackLink;
        String newPassword;

        public Builder() {
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setAccountname(String accountname) {
            this.accountname = accountname;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setAuthorities(List<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder setCallbackLink(String callbackLink) {
            this.callbackLink = callbackLink;
            return this;
        }

        public Builder setNewPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        @Override
        public Builder merge(User org) {
            Builder bld = super.merge(org)
                    .setAccountname(StringUtils.defaultIfBlank(this.accountname, org.getAccountname()))
                    .setEmail(StringUtils.defaultIfBlank(this.email, org.getEmail()))
                    .setEnabled(ObjectUtils.defaultIfNull(this.enabled, org.getEnabled()))
                    .setLastName(StringUtils.defaultIfBlank(this.lastName, org.getLastName()))
                    .setUsername(StringUtils.defaultIfBlank(this.username, org.getUsername()))
                    .setFirstName(StringUtils.defaultIfBlank(this.firstName, org.getFirstName()))
                    .setPassword(StringUtils.defaultIfBlank(this.password, org.getPassword()))
                    .setPhone(StringUtils.defaultIfBlank(this.phone, org.getPhone()))
                    .setAuthorities(ObjectUtils.defaultIfNull(this.authorities, org.getAuthorities()));

            return bld;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    public static Builder from(User user) {
        Builder builder = new Builder();
        BeanUtils.copyProperties(user, builder);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }
}
