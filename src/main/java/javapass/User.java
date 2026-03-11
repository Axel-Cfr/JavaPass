package javapass;

public class User {
    private String username;
    private byte[] key;
    private String last_login;

    User(String username, byte[] key, String last_login) {
        this.username = username;
        this.key = key;
        this.last_login = last_login;
    }
}
