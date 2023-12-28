package com.example.fectdo.utils;

public class YouTubeLinkConverter {

    public static String convertToEmbedLink(String youtubeLink) {
        // Extract video ID from the YouTube link
        String videoId = extractVideoId(youtubeLink);

        if (videoId == null) {
            // Handle the case of an invalid or unsupported link
            System.out.println("Invalid or unsupported YouTube link: " + youtubeLink);
            return null;
        }

        // Construct the embed link
        return "https://www.youtube.com/embed/" + videoId;
    }

    public static String extractVideoId(String youtubeLink) {
        String videoId = null;

        // Extract video ID based on the link format
        if (youtubeLink != null && youtubeLink.trim().length() > 0) {
            if (youtubeLink.contains("youtu.be")) {
                // Extract video ID from share link
                String[] urlParts = youtubeLink.split("//");
                if(urlParts.length > 1){
                    urlParts = urlParts[1].split("/");
                }
                if (urlParts.length > 1) {
                    videoId = urlParts[1];
                    int questionMarkPosition = videoId.indexOf('?');
                    if (questionMarkPosition != -1) {
                        videoId = videoId.substring(0, questionMarkPosition);
                    }
                }
            } else if (youtubeLink.contains("youtube.com")) {
                // Extract video ID from standard link
                String[] urlParts = youtubeLink.split("v=");
                if (urlParts.length > 1) {
                    videoId = urlParts[1];
                    int ampersandPosition = videoId.indexOf('&');
                    if (ampersandPosition != -1) {
                        videoId = videoId.substring(0, ampersandPosition);
                    }
                }
            }
        }

        return videoId;
    }
}
