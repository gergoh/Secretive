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

public class RailFenceCipher extends AppCompatActivity {

    private String plainText;
    private String cipherText;
    private int inputKey = 0;

    private boolean invalidKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_fence);
    }

    // Action when user clicks Encryption button
    public void encryptClicked(View view) {
        getData();
        if (invalidKey) return; // If entered key value is invalid, the method stops
        cipherText = encrypt(plainText, inputKey);

        startOutput();
    }

    // Action when user clicks Decryption button
    public void decryptClicked(View view) {
        getData();
        if (invalidKey) return; // If entered key value is invalid, the method stops
        cipherText = decrypt(plainText, inputKey);

        startOutput();
    }

    // Get all text entered by user
    private void getData() {
        // What user entered into userInput and keyValue EditText is saved into local variables
        plainText = ((EditText) findViewById(R.id.userInput)).getText().toString();
        inputKey = Integer.parseInt(((EditText) findViewById(R.id.keyValue)).getText().toString());

        // Stores number of letters
        int letters = (plainText.replaceAll("[^a-zA-Z]+", "")).length();

        // TODO Experiment with input validation and error messages
        // Checking if key value is between 0-26
        if (inputKey > letters) {
            Toast.makeText(this, "Key cannot be more than number of letters", Toast.LENGTH_LONG).show();
            //((EditText) findViewById(R.id.keyValue)).setError("Invalid Key Value");
            invalidKey = true;
        } else invalidKey = false;
    }

    // TODO Add 'X's as placeholders so that there are the same number of letters on the top row, as on the bottom row
    // Rail Fence encryption method
    private String encrypt(String text, int key) {
        // Formats plaintext into consecutive uppercase letters
        String formattedText = (text.replaceAll("[^a-zA-Z]+", "")).toUpperCase();

        // Converts plaintext into multi-row table depending on key value
        // zig-zag pattern
        char[][] cipherTable = new char[key][formattedText.length()];
        for (int columnCounter = 0, rowCounter = 0; columnCounter < formattedText.length(); ) {
            for (rowCounter = 0; rowCounter < key; rowCounter++, columnCounter++) {
                if (columnCounter >= formattedText.length()) break;
                cipherTable[rowCounter][columnCounter] = formattedText.charAt(columnCounter);
            }
            for (rowCounter -= 2; rowCounter > 0; rowCounter--, columnCounter++) {
                if (columnCounter >= formattedText.length()) break;
                cipherTable[rowCounter][columnCounter] = formattedText.charAt(columnCounter);
            }
        }

        // Convert multi-row ciphertext into a single line
        char[] cipherText = new char[formattedText.length()];
        for (int row = 0, itr = 0; row < key; row++) {
            for (int column = 0; column < cipherTable[0].length; column++) {
                if (Character.isLetter(cipherTable[row][column])) {
                    cipherText[itr] = cipherTable[row][column];
                    itr++;
                }
            }
        }

        // Converts finished single line ciphertext char array into String and returns it
        return new String(cipherText);

    }

    // Rail Fence decryption method
    private String decrypt(String text, int key) {
        char [] decodedText = new char[text.length()];
        int cycle = (key * 2) - 2;

        for (int i = 0; i < text.length(); i++) {
            for (int j = 0, letter = 0; i + j < text.length(); j += cycle, letter++) {
                decodedText[i + j] = text.charAt(letter);
            }
        }

        return new String(decodedText);
    }

    // Changes layout to output_layer
    // Sets up output layout text and copy button
    private void startOutput() {
        // Changing layout to show output text
        setContentView(R.layout.output_layout);

        // TextView in output_layout is set to the final encrypted/decrypted text
        TextView outputView = (TextView) findViewById(R.id.finalOutput);
        outputView.setText(cipherText);

        // Setting up Copy button to copy to clipboard
        Button copyButton = (Button) findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("outputText", cipherText));
                Toast.makeText(RailFenceCipher.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });
    }
}
