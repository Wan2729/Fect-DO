    package com.example.fectdo.social.findfriends;

    public class FindFriendModel {

        private String userName;
        private String photoUri;
        private String userID;
        private boolean requestSent;

        public FindFriendModel(String userName, String photoUri, String userID, boolean requestSent) {
            this.userName = userName;
            this.photoUri = photoUri;
            this.userID = userID;
            this.requestSent = requestSent;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(String photoUri) {
            this.photoUri = photoUri;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public boolean isRequestSent() {
            return requestSent;
        }

        public void setRequestSent(boolean requestSent) {
            this.requestSent = requestSent;
        }
    }
