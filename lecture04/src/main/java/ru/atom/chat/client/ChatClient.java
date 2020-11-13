package ru.atom.chat.client;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.IOException;


public class ChatClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    //POST host:port/chat/signup?name=my_name&password=my_password
    public static Response signup(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/signup?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/login?name=my_name&password=my_password
    public static Response login(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/login?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/online
    public static Response viewOnline() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/online")
                .addHeader("host", HOST + PORT)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/allusers
    public static Response viewAllUsers() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/allusers")
                .addHeader("host", HOST + PORT)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/logout?name=my_name&password=my_password
    public static Response logout(String name, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/logout?name=" + name + "&password=" + password)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/say?name=my_name&password=my_password
    //Body: "msg='my_message'"
    public static Response say(String name, String password, String message) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/say?name=" + name + "&password=" + password + "&msg=" + message)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/chat/chat
    public static Response viewChat() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/chat")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/chat/admin/kick?adminname=adminname&password=password
    //Body: "kickname='kickname'"
    public static Response adminKick(String adminName, String password, String kickName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/admin/kick?adminname=" + adminName + "&password="
                        + password + "&kickname=" + kickName)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/chat/admin/ban?adminname=adminname&password=password
    //Body: "banname='banname'"
    public static Response adminBan(String adminName, String password, String banName) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/chat/admin/ban?adminname=" + adminName + "&password="
                        + password + "&banname=" + banName)
                .build();

        return client.newCall(request).execute();
    }
}