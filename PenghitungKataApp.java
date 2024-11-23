package com.example.java;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PenghitungKataApp extends JFrame {
    private JTextArea inputTextArea;
    private JLabel wordCountLabel, charCountLabel, sentenceCountLabel, paragraphCountLabel;
    private JTextField searchTextField;
    private JLabel searchCountLabel;
    private JButton calculateButton, saveButton;

    public PenghitungKataApp() {
        setTitle("Penghitung Kata");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Teks"));
        inputTextArea = new JTextArea(12, 45);
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(inputTextArea);
        inputPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Hasil"));
        wordCountLabel = new JLabel("Jumlah Kata: 0");
        charCountLabel = new JLabel("Jumlah Karakter: 0");
        sentenceCountLabel = new JLabel("Jumlah Kalimat: 0");
        paragraphCountLabel = new JLabel("Jumlah Paragraf: 0");
        resultPanel.add(wordCountLabel);
        resultPanel.add(charCountLabel);
        resultPanel.add(sentenceCountLabel);
        resultPanel.add(paragraphCountLabel);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Pencarian Kata"));
        searchTextField = new JTextField(20);
        searchCountLabel = new JLabel("Kemunculan: 0");
        searchPanel.add(new JLabel("Cari Kata:"));
        searchPanel.add(searchTextField);
        searchPanel.add(searchCountLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        calculateButton = new JButton("Hitung");
        saveButton = new JButton("Simpan");
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);

        calculateButton.addActionListener(e -> calculateText());
        searchTextField.addActionListener(e -> searchWord());
        saveButton.addActionListener(e -> saveToFile());

        inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateText();
            }
        });
    }

    private void calculateText() {
        String text = inputTextArea.getText().trim();
        String[] words = text.isEmpty() ? new String[0] : text.split("\\s+");
        int wordCount = words.length;
        int charCount = text.length();
        int sentenceCount = text.split("[.!?]").length;
        int paragraphCount = text.split("\\n").length;

        wordCountLabel.setText("Jumlah Kata: " + wordCount);
        charCountLabel.setText("Jumlah Karakter: " + charCount);
        sentenceCountLabel.setText("Jumlah Kalimat: " + sentenceCount);
        paragraphCountLabel.setText("Jumlah Paragraf: " + paragraphCount);
    }

    private void searchWord() {
        String text = inputTextArea.getText();
        String word = searchTextField.getText().trim();

        if (word.isEmpty()) {
            searchCountLabel.setText("Kemunculan: 0");
            return;
        }

        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        searchCountLabel.setText("Kemunculan: " + count);
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(inputTextArea.getText());
                writer.newLine();
                writer.write("Hasil Perhitungan:");
                writer.newLine();
                writer.write(wordCountLabel.getText());
                writer.newLine();
                writer.write(charCountLabel.getText());
                writer.newLine();
                writer.write(sentenceCountLabel.getText());
                writer.newLine();
                writer.write(paragraphCountLabel.getText());
                JOptionPane.showMessageDialog(this, "Teks berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PenghitungKataApp app = new PenghitungKataApp();
            app.setVisible(true);
        });
    }
}
