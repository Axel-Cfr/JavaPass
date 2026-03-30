package javapass;

import java.util.ArrayList;

public class User {
    private String username;
    private byte[] key;
    private String last_login;
    private ArrayList<SQLite.MdpValues> passwordList;

    User(String username, byte[] key, String last_login, ArrayList<SQLite.MdpValues> passwordList) {
        this.username = username;
        this.key = key;
        this.last_login = last_login;
        this.passwordList = passwordList;
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

    public int getPasswordValues(String websiteName) {
        for(int i = 0; i < passwordList.size(); i++) {
            if(websiteName.equals(passwordList.get(i).getWebsite_name())) {
                return i;
            }
        }
        return -1;
    }

    public String getWebsiteName(int i) {
        return passwordList.get(i).getWebsite_name();
    }

    public String getUrl(int i) {
        return passwordList.get(i).getUrl();
    }

    public String getEncryptedUsername(int i) {
        return passwordList.get(i).getEncrypted_username();
    }

    public String getEncryptedPassword(int i) {
        return passwordList.get(i).getEncrypted_password();
    }

    public byte[] getIvUsername(int i) {
        return passwordList.get(i).getIv_username();
    }

    public byte[] getIvPassword(int i) {
        return passwordList.get(i).getIv_password();
    }

    public ArrayList<String> getPasswordList() {
        ArrayList<String> websiteNameList = new ArrayList<String>();
        for(int i = 0; i < passwordList.size(); i++) {
            websiteNameList.add(passwordList.get(i).getWebsite_name());
        }
        return websiteNameList;
    }
}
