package bigchicken.secretive;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayfairCipher extends AppCompatActivity {

    private String plainText;
    private String cipherText;
    private String inputKey;

    // 5x5 key table
    private char[][] keyTable = new char[5][5];

    // Boolean values for which rule we need to use
    private boolean columnRule = false,
                    rowRule = false,
                    thirdRule = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playfair);
    }

    // Action when user clicks Encryption button
    public void encryptClicked(View view) {
        getData(plainText);
        cipherText = encrypt(plainText);
        startOutput(cipherText);
    }

    // Action when user clicks Decryption button
    public void decryptClicked(View view) {
        getData(cipherText);
        plainText = decrypt(cipherText);
        startOutput(plainText);
    }

    // Get all text entered by user
    private void getData(String inputText,){
        // What user entered into userInput and keyValue EditText is saved into local variables
        plainText = ((EditText) findViewById(R.id.userInput)).getText().toString();
        inputKey = ((EditText) findViewById(R.id.keyValue)).getText().toString();
    }

    // Playfair Cipher Encryption method
    private String encrypt(String text) {
        // Stores the number of alphabetic letters in input
        int letters = (text.replaceAll("[^a-zA-Z]+", "")).length();

        // If there are uneven number of letters in input, increment it by one
        // Later when we declare size of formattedInput if uneven number is divided
        // Java rounds down and in that case we would lose a letter
        if (letters % 2 != 0) letters++;

        // Format input into two letter chunks
        // If uneven letters, 'Z' is added to last chunk
        char[][] formattedInput = new char[letters / 2][2];
        for (int i = 0, itr = 0; i < text.length(); i++){
            for (int j = 0; j < 2; j++){
                while (!Character.isLetter(text.charAt(i))) i++;
                formattedInput[itr++][j] = text.toUpperCase().charAt(i);
                if (i == text.length() - 1) formattedInput[itr][++j] = 'Z';
            }
        }

        fillKeyTable(keyTable);

        // Finding correct rule and finding index in key table
        int[] firstIndex = new int[2];
        int[] secondIndex = new int[2];
        char [] encrypted = new char[formattedInput.length * 2];
        for(int i = 0, j = 0; i < formattedInput.length; i++, j++){
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (formattedInput[i][0] == keyTable[r][c]) {
                        firstIndex[0] = r;
                        firstIndex[1] = c;
                    }
                    if (formattedInput[i][1] == keyTable[r][c]) {
                        secondIndex[0] = r;
                        secondIndex[1] = c;
                    }
                }
            }

            // Getting correct encrypted letter from key table
            findRule(keyTable, formattedInput[i]);
            if(columnRule){
                if(firstIndex[0] == 4) firstIndex[0] = -1;
                encrypted[j++] = keyTable[firstIndex[0] + 1][firstIndex[1]];
                if(secondIndex[0] == 4) secondIndex[0] = -1;
                encrypted[j] = keyTable[secondIndex[0] + 1][secondIndex[1]];
            }
            if(rowRule){
                if(firstIndex[1] == 4) firstIndex[1] = -1;
                encrypted[j++] = keyTable[firstIndex[0]][firstIndex[1] + 1];
                if(secondIndex[1] == 4) secondIndex[1] = -1;
                encrypted[j] = keyTable[secondIndex[0]][secondIndex[1] + 1];
            }
            if(thirdRule){
                encrypted[j++] = keyTable[firstIndex[0]][secondIndex[1]];
                encrypted[j] = keyTable[secondIndex[0]][firstIndex[1]];
            }
        }

        return new String(encrypted);
    }

    // Takes the key and fills the 5x5 key table
    public char[][] fillKeyTable(char[][] table) {
        // Placing key text into beginning of key table
        tableloop:
        for (int i = 0; i < inputKey.length();) {
            for (int row = 0; row < 5; row++) {
                for (int column = 0; column < 5; column++) {
                    while (Character.isWhitespace(inputKey.charAt(i))) i++;
                    while (duplicateCheck(table, i)) i++;
                    table[row][column] = inputKey.toUpperCase().charAt(i++);
                    if (i >= inputKey.length()) break tableloop;
                }
            }
        }

        // Putting missing letters of ABC into key table following the text letters
        // Q is omitted (table is only 25 letters)
        // r(ow) and c(olumn) variable set to the next empty index
        int r = 0;
        int c = 0;
        findemptyloop:
        for (; r < 5; r++) {
            for (; c < 5; c++) if (table[r][c] == 0) break findemptyloop;
            c = 0;
        }

        // If text that was put into key table ends in the middle of a row, fill it separately
        char ch = 'A';
        if (c != 0) {
            for (; c < 5; c++) {
                while (duplicateCheck(table, ch)) ch++;
                if (ch != 'Q') table[r][c] = ch++;
            }
            r++;
        }

        // Rest of the key table is filled with ABC
        for(; r < 5; r++) {
            for (int column = 0; column < 5; column++) {
                if (ch == 'Q') ch++;
                while (duplicateCheck(table, ch)) ch++;
                table[r][column] = ch++;
            }
        }

        return table;
    }

    // Method iterates through input to check which rule we need to use on each chunk
    private void findRule(char [][] table, char [] chunk) {
        // Resetting all boolean values to false
        columnRule = false;
        rowRule = false;
        thirdRule = false;

        // Storing indexes of letters of chunks in key table to later compare positions and decide rule
        int[] firstIndex = new int[2];
        int[] secondIndex = new int[2];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (chunk[0] == table[r][c]) {
                    firstIndex[0] = r;
                    firstIndex[1] = c;
                }
                if (chunk[1] == table[r][c]) {
                    secondIndex[0] = r;
                    secondIndex[1] = c;
                }
            }
        }

        if (firstIndex[1] == secondIndex[1]) columnRule = true;
        else if(firstIndex[0] == secondIndex[0]) rowRule = true;
        else thirdRule = true;
    }

    // Checks if any letters that are about to be added to the key table are not already there to avoid duplicates
    private boolean duplicateCheck(char[][] table, int itr) {
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                if (table[row][column] == inputKey.toUpperCase().charAt(itr)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Function overload for when char is used as iterator
    private boolean duplicateCheck(char[][] table, char ch) {
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                if (table[row][column] == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    // Playfair Cipher Decryption method
    private String decrypt(String text) {
        // Stores the number of alphabetic letters in input
        int letters = (text.replaceAll("[^a-zA-Z]+", "")).length();

        // Format input into two letter chunks
        char[][] formattedInput = new char[letters / 2][2];
        for (int i = 0, itr = 0; i < text.length(); i++) {
            for (int j = 0; j < 2; j++) {
                while (!Character.isLetter(text.charAt(i))) i++;
                formattedInput[itr++][j] = text.toUpperCase().charAt(i);
            }
        }

        fillKeyTable(keyTable);

        // Finding correct rule and finding index in key table
        int[] firstIndex = new int[2];
        int[] secondIndex = new int[2];
        char [] decrypted = new char[formattedInput.length * 2];
        for(int i = 0, j = 0; i < formattedInput.length; i++, j++){
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (formattedInput[i][0] == keyTable[r][c]) {
                        firstIndex[0] = r;
                        firstIndex[1] = c;
                    }
                    if (formattedInput[i][1] == keyTable[r][c]) {
                        secondIndex[0] = r;
                        secondIndex[1] = c;
                    }
                }
            }

            // Getting correct decrypted letter from key table
            findRule(keyTable, formattedInput[i]);
            if(columnRule){
                if(firstIndex[0] == 0) firstIndex[0] = 5;
                decrypted[j++] = keyTable[firstIndex[0] - 1][firstIndex[1]];
                if(secondIndex[0] == 0) secondIndex[0] = 5;
                decrypted[j] = keyTable[secondIndex[0] - 1][secondIndex[1]];
            }
            if(rowRule){
                if(firstIndex[1] == 0) firstIndex[1] = 5;
                decrypted[j++] = keyTable[firstIndex[0]][firstIndex[1] - 1];
                if(secondIndex[1] == 0) secondIndex[1] = 5;
                decrypted[j] = keyTable[secondIndex[0]][secondIndex[1] - 1];
            }
            if(thirdRule){
                decrypted[j++] = keyTable[firstIndex[1]][secondIndex[0]];
                decrypted[j] = keyTable[secondIndex[1]][firstIndex[0]];
            }
        }

        return new String(decrypted);
    }

    // Changes layout to output_layer
    // Sets up output layout text and copy button
    private void startOutput(String outputText){
        // Changing layout to show output text
        setContentView(R.layout.output_layout);

        // TextView in output_layout is set to the final encrypted/decrypted text
        TextView outputView = (TextView) findViewById(R.id.finalOutput);
        outputView.setText(outputText);

        // Setting up Copy button to copy to clipboard
        Button copyButton = (Button) findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("outputText", cipherText));
                Toast.makeText(PlayfairCipher.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });
    }
}