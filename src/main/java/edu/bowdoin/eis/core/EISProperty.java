package edu.bowdoin.eis.core;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EISProperty {

    private static final String ENCRYPTOR_PASSWORD = System.getenv("ENCRYPTOR_PASSWORD");

    @Id
    private String key;
    @Column(nullable = false)
    private String value;
    private Boolean secret = false;

    public EISProperty() {}

    public EISProperty(String key, String value) {
        this(key, value, false);
    }

    public EISProperty(String key, String value, Boolean secret) {
        this.key = key;
        this.value = value;
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecretValue() {
        if (secret) {
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(ENCRYPTOR_PASSWORD);
            return textEncryptor.decrypt(this.value);
        }

        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSecretValue(String secret) {
        this.secret = true;

        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(ENCRYPTOR_PASSWORD);

        this.value = textEncryptor.encrypt(secret);
    }

    public Boolean getSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }
}
