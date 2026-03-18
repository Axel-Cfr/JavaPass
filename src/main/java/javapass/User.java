package javapass;

public class User {
    private String username;
    private byte[] key;
    private String last_login;
    private String[] websiteNames;
    private String[] urls;
    private String[] encryptedUsernames;
    private String[] encryptedPasswords;
    private byte[][] ivsUsername;
    private byte[][] ivsPassword;

    User(String username, byte[] key, String last_login, String[] websiteNames, String[] urls, String[] encryptedUsernames, String[] encryptedPasswords, byte[][] ivsUsername, byte[][] ivsPassword) {
        this.username = username;
        this.key = key;
        this.last_login = last_login;
        this.websiteNames = websiteNames;
        this.urls = urls;
        this.encryptedUsernames = encryptedUsernames;
        this.encryptedPasswords = encryptedPasswords;
        this.ivsUsername = ivsUsername;
        this.ivsPassword = ivsPassword;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getKey() {
        return key;
    }

    public String getLast_login() {
        return last_login;
    }

    public String[] getWebsiteNames() {
        return websiteNames;
    }

    public String[] getUrls() {
        return urls;
    }

    public String[] getEncryptedUsernames() {
        return encryptedUsernames;
    }

    public String[] getEncryptedPasswords() {
        return encryptedPasswords;
    }

    public byte[][] getIvsUsername() {
        return ivsUsername;
    }

    public byte[][] getIvsPassword() {
        return ivsPassword;
    }
}
