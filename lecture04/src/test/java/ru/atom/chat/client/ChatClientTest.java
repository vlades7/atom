package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.server.ChatApplication;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String ADMIN_NAME = "admin";
    private static String ADMIN_PASSWORD = "admin";
    private static String LOSSER_NAME = "Bully";
    private static String LOSSER_PASSWORD = "BullyPassword";
    private static String MY_NAME_IN_CHAT = "Test_name";
    private static String MY_PASSWORD_IN_CHAT = "Test_password";
    private static String MY_MESSAGE_TO_CHAT = "Test_msg";


    @Test
    public void signup() throws IOException {
        Response response = ChatClient.signup(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 200
                || body.equals("This user already exists"));
    }

    @Test
    public void login() throws IOException {
        ChatClient.signup(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        Response response = ChatClient.login(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        System.out.println();
        Assert.assertTrue(response.code() == 200
                || body.equals("This user doesn't exists")
                || body.equals("Already logged in."));
    }

    @Test
    public void viewOnline() throws IOException {
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void viewAllUsers() throws IOException {
        Response response = ChatClient.viewAllUsers();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void logout() throws IOException {
        ChatClient.signup(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        ChatClient.login(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        Response response = ChatClient.logout(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void say() throws IOException {
        ChatClient.signup(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        ChatClient.login(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT);
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_PASSWORD_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void viewChat() throws IOException {
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void adminKick() throws IOException {
        ChatClient.signup(LOSSER_NAME, LOSSER_PASSWORD);
        ChatClient.login(LOSSER_NAME, LOSSER_PASSWORD);
        Response response = ChatClient.adminKick(ADMIN_NAME, ADMIN_PASSWORD, LOSSER_NAME);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void adminBan() throws IOException {
        ChatClient.signup(LOSSER_NAME, LOSSER_PASSWORD);
        ChatClient.login(LOSSER_NAME, LOSSER_PASSWORD);
        Response response = ChatClient.adminBan(ADMIN_NAME, ADMIN_PASSWORD, LOSSER_NAME);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }
}
