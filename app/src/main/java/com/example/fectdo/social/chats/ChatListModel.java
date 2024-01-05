package com.example.fectdo.social.chats;

public class ChatListModel {

    private String userId;
    private String userName;
    private String photoName;
    private String unreadCount;
    private String lastMessage;
    private String lastMessageTimes;

    public ChatListModel(String userId, String userName,String photoName, String unreadCount, String lastMessage, String lastMessageTimes) {
        this.userId = userId;
        this.userName = userName;
        this.photoName = photoName;
        this.unreadCount = unreadCount;
        this.lastMessage = lastMessage;
        this.lastMessageTimes = lastMessageTimes;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTimes() {
        return lastMessageTimes;
    }

    public void setLastMessageTimes(String lastMessageTimes) {
        this.lastMessageTimes = lastMessageTimes;
    }
}
