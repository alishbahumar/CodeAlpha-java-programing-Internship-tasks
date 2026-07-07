import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;

public class ChatbotGUI extends JFrame {

    // ---------- Chatbot brain (string processing / rule-based logic) ----------
    static class Chatbot {

        String botName = "CodeBot";

        // Generate a response based on user input using keyword matching
        String getResponse(String userInput) {
            if (userInput == null || userInput.trim().isEmpty()) {
                return "Please type something so I can help you!";
            }

            String input = userInput.toLowerCase().trim();

            // Greetings
            if (containsAny(input, "hello", "hi", "hey", "salam", "assalam")) {
                return "Hello! I'm " + botName + ". How can I help you today?";
            }

            // Asking bot's name
            if (containsAny(input, "your name", "who are you")) {
                return "I'm " + botName + ", a simple rule-based Java chatbot!";
            }

            // Asking how bot is doing
            if (containsAny(input, "how are you")) {
                return "I'm just a program, but I'm running great! How about you?";
            }

            // Time
            if (containsAny(input, "time")) {
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                return "The current time is " + time + ".";
            }

            // Date
            if (containsAny(input, "date", "today")) {
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
                return "Today's date is " + date + ".";
            }

            // Help / FAQ
            if (containsAny(input, "help", "what can you do")) {
                return "I can chat about greetings, tell you the time/date, tell a joke, "
                        + "or talk about Java programming. Try asking me something!";
            }

            // Joke
            if (containsAny(input, "joke", "funny")) {
                return randomChoice(
                        "Why do programmers prefer dark mode? Because light attracts bugs!",
                        "Why did the Java developer wear glasses? Because they couldn't C#!",
                        "There are only 10 types of people: those who understand binary and those who don't."
                );
            }

            // Java related FAQ
            if (containsAny(input, "java")) {
                return "Java is an object-oriented programming language. Are you learning it for a project?";
            }
            if (containsAny(input, "arraylist")) {
                return "An ArrayList is a resizable array from java.util package, useful when list size can change.";
            }
            if (containsAny(input, "oop", "object oriented")) {
                return "OOP stands for Object-Oriented Programming. Its 4 pillars are Encapsulation, "
                        + "Inheritance, Polymorphism, and Abstraction.";
            }

            // Thanks
            if (containsAny(input, "thank")) {
                return "You're welcome! Happy to help.";
            }

            // Goodbye
            if (containsAny(input, "bye", "goodbye", "exit", "quit")) {
                return "Goodbye! Have a great day. (You can close the window now)";
            }

            // Default fallback response
            return randomChoice(
                    "I'm not sure I understand. Can you rephrase that?",
                    "Interesting! Tell me more, or type 'help' to see what I can do.",
                    "Hmm, I don't have an answer for that yet."
            );
        }

        // Helper: checks if input contains any of the given keywords
        boolean containsAny(String input, String... keywords) {
            for (String k : keywords) {
                if (input.contains(k)) return true;
            }
            return false;
        }

        // Helper: picks a random response from options (adds variety)
        String randomChoice(String... options) {
            Random rand = new Random();
            return options[rand.nextInt(options.length)];
        }
    }

    // ---------- GUI components ----------
    Chatbot bot = new Chatbot();
    JTextArea chatArea;
    JTextField inputField;

    public ChatbotGUI() {
        setTitle("AI Chatbot - " + bot.botName);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Send button
        JButton sendButton = new JButton("Send");

        // Bottom panel (input field + send button)
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Welcome message
        appendMessage(bot.botName, "Hello! I'm " + bot.botName + ". Ask me anything, or type 'help'.");

        // Action: Send button click
        sendButton.addActionListener(this::handleSend);

        // Action: Enter key press in input field
        inputField.addActionListener(this::handleSend);
    }

    void handleSend(ActionEvent e) {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        appendMessage("You", userText);
        String response = bot.getResponse(userText);
        appendMessage(bot.botName, response);

        inputField.setText("");
    }

    void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (best practice for Swing apps)
        SwingUtilities.invokeLater(() -> {
            ChatbotGUI gui = new ChatbotGUI();
            gui.setVisible(true);
        });
    }
}