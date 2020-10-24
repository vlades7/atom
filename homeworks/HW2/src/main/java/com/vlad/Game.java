package com.vlad;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private static final Random random = new Random();
    public String word;
    private List<String> words;

    public Game(List<String> words) {
        this.words = words;
    }

    private String chooseSecretWord() {
        return words.get(random.nextInt(words.size()));
    }

    /**
     * Вычисляем количество коров и быков
     */
    public int[] computeCowsBulls(String guess) {
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                bulls++;
            } else if (word.indexOf(guess.charAt(i)) != -1) {
                cows++;
            }
        }
        return new int[]{cows, bulls};
    }

    /**
     * Старт
     */
    void start() {
        System.out.println("Welcome to The Bulls & Cows, player!");
        String playAgain;
        Scanner scanner = new Scanner(System.in);
        do {
            word = chooseSecretWord();
            System.out.println("I offered a " + word.length() + "-letter word, your guess?");
            playRound(scanner);
            System.out.println("Wanna play again? Yes/No");
            playAgain = scanner.next();
        } while (playAgain.equalsIgnoreCase("Yes"));
    }

    /**
     * Играть
     */
    private void playRound(Scanner scanner) {
        int losses = 0;
        while (losses < 10) {
            String guess = scanner.next();
            if (guess.length() != word.length()) {
                System.out.println("Incorrect word length. Try again.");
                continue;
            }
            if (guess.equals(word)) {
                System.out.println("Wow! You won!");
                return;
            }
            System.out.println("Cows: " + computeCowsBulls(guess)[0]);
            System.out.println("Bulls: " + computeCowsBulls(guess)[1]);
            losses++;
        }

        List<String> textForLoosers = Arrays.asList(
                "You lose",
                "I'm sorry, but you lost.",
                "Unfortunately, you didn't succeed.",
                "Nice try, but you failed.",
                "You have lost, but there is no need to lose heart.",
                "YOU LOSE!!!",
                "After" + losses + "attempts, you failed to guess the word. Sadly :("
        );
        System.out.println(textForLoosers.get(new Random().nextInt(textForLoosers.size())));
    }
}