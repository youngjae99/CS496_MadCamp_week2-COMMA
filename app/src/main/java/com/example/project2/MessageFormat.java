package com.example.project2;

public class MessageFormat {

    private String Username;
    private String Message;
    private String Hide;

    public MessageFormat(String username, String message, String hide) {
        Username = username;
        Message = message;
        Hide = hide;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getHide() {
        return Hide;
    }

    public void setHide(String hide) {
        Hide = hide;
    }
}
