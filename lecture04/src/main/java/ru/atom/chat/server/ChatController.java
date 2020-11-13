package ru.atom.chat.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import java.security.MessageDigest;

@Controller
@RequestMapping("chat")
public class ChatController {
    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> registeredUsers = new ConcurrentHashMap<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private String adminPassword = getSha256("admin");

    ChatController() {
        registeredUsers.put("admin", adminPassword);
    }

    /**
     * curl -X POST -i localhost:8080/chat/signup -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "signup",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> signup(@RequestParam("name") String name,
                                        @RequestParam("password") String password) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name.");
        }

        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name.");
        }

        if (password.length() < 8) {
            return ResponseEntity.badRequest().body("Too short password.");
        }

        if (password.length() > 16) {
            return ResponseEntity.badRequest().body("Too long password.");
        }

        if (registeredUsers.containsKey(name)) {
            return ResponseEntity.badRequest().body("This user already exists");
        }

        if (!registeredUsers.containsKey("admin")) {
            registeredUsers.put("admin", getSha256(adminPassword));
        }

        registeredUsers.put(name, getSha256(password));
        messages.add("[" + name + "] registered! Welcome to our chat!");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name,
                                        @RequestParam("password") String password) {
        if (!registeredUsers.containsKey(name)) {
            return ResponseEntity.badRequest().body("This user doesn't exists");
        }

        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in.");
        }

        if ((name.equals("admin")) && (!getSha256(password).equals(adminPassword))) {
            return ResponseEntity.badRequest().body("Incorrect Admin password.");
        }

        usersOnline.put(name, getSha256(password));
        messages.add("[" + name + "] logged in");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/allusers
     */
    @RequestMapping(
            path = "allusers",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity allUsers() {
        String responseBody = String.join("\n", registeredUsers.keySet().stream().sorted().collect(Collectors.toList()));

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=NAME&password=PASSWORD"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("name") String name,
                                         @RequestParam("password") String password) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged out.");
        }

        if (getSha256(password).equals(usersOnline.get(name))) {
            usersOnline.remove(name);
            messages.add("[" + name + "] logged out.");
        } else {
            return ResponseEntity.badRequest().body("Wrong password.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=NAME&password=PASSWORD&msg=MESSAGE"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> say(@RequestParam("name") String name,
                                      @RequestParam("password") String password,
                                      @RequestParam("msg") String msg) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User logged out.");
        }

        if (getSha256(password).equals(usersOnline.get(name))) {
            messages.add("[" + name + "]: " + msg);
        } else {
            return ResponseEntity.badRequest().body("Wrong password.");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity chat() {
        String responseBody = String.join("\n", messages);

        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/admin/kick -d "adminname=ADMINNAME&password=PASSWORD&kickname=KICKNAME"
     */
    @RequestMapping(
            path = "admin/kick",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity adminKick(@RequestParam("adminname") String adminName,
                                    @RequestParam("password") String password,
                                    @RequestParam("kickname") String kickName) {
        if ((!adminName.equals("admin")) || (!getSha256(password).equals(adminPassword))) {
            return ResponseEntity.badRequest().body("Incorrect Admin password.");
        }

        if (!usersOnline.containsKey(kickName)) {
            return ResponseEntity.badRequest().body("User logged out.");
        }

        usersOnline.remove(kickName);
        messages.add("[" + kickName + "] was kicked by Admin.");

        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/admin/ban -d "adminname=ADMINNAME&password=PASSWORD&banname=BANNAME"
     */
    @RequestMapping(
            path = "admin/ban",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity adminBan(@RequestParam("adminname") String adminName,
                                    @RequestParam("password") String password,
                                    @RequestParam("banname") String banName) {
        if ((!adminName.equals("admin")) || (!getSha256(password).equals(adminPassword))) {
            return ResponseEntity.badRequest().body("Incorrect Admin password.");
        }

        if (usersOnline.containsKey(banName)) {
            usersOnline.remove(banName);
        }

        if (!registeredUsers.containsKey(banName)) {
            return ResponseEntity.badRequest().body("[" + banName + "] doesn't exists");
        }

        registeredUsers.remove(banName);
        messages.add("[" + banName + "] was banned by Admin. Bye-bye!");

        return ResponseEntity.ok().build();
    }

    private String getSha256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}