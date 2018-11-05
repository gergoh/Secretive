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

public class VigenereCipher extends AppCompatActivity {

    private String plainText;
    private String cipherText;
    private String inputKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vigenere);
    }

    // Action when user clicks Encryption button
    public void encryptClicked(View view) {
        getData();
        cipherText = encrypt(plainText, inputKey);

        startOutput();
    }

    // Action when user clicks Decryption button
    public void decryptClicked(View view) {
        getData();
        cipherText = decrypt(plainText, inputKey);

        startOutput();
    }

    // Get all text entered by user
    private void getData(){
        // What user entered into userInput and keyValue EditText is saved into local variables
        plainText = ((EditText) findViewById(R.id.userInput)).getText().toString();
        inputKey = ((EditText) findViewById(R.id.keyValue)).getText().toString();
    }

    // Vigenere Cipher Encryption method
    public String encrypt(String text, String key) {
        // Copying input text and key into char arrays so later we can access them by index
        char[] charText = text.toCharArray();
        char[] keyArray = key.toCharArray();

        int ASCII; // A temporarily holder for the converted ASCII values
        int shift; // A holder for the amount the text needs to be shifted
        int shiftIter = 0; // Key needs separate iterator so it can loop over

        // If it isn't an alphabetic letter, it keeps it unchanged
        for (int i = 0; i < charText.length; i++) {
            // Looping the key characters if text is longer
            if (i >= keyArray.length && shiftIter >= keyArray.length) shiftIter = 0;
            if (Character.isUpperCase(charText[i])) {
                // ASCII value of key (in int)
                shift = (int) keyArray[shiftIter];
                // -65 so it becomes its number in the alphabet
                shift -= 65;
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) + shift);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 90) ASCII -= 26;
                if (ASCII < 65) ASCII += 26;
                // ASCII value converted into its char representation and charText with index = i is updated
                charText[i] = (char) ASCII;
            }
            if (Character.isLowerCase(charText[i])) {
                // ASCII value of key (in int)
                shift = (int) keyArray[shiftIter];
                // -97 so it becomes its number in the alphabet
                shift -= 97;
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) + shift);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 122) ASCII -= 26;
                if (ASCII < 97) ASCII += 26;
                // ASCII value converted into its char representation and charText with index = i is updated
                charText[i] = (char) ASCII;
            }

            // Key's iterator is incremented
            shiftIter++;
        }

        // The finished charText is converted into a String and returned
        return new String(charText);
    }

    // Vigenere Cipher Decryption method
    public  String decrypt(String text, String key) {
        // Copying input text and key into char arrays so later we can access them by index
        char[] charText = text.toCharArray();
        char[] keyArray = key.toCharArray();

        int ASCII; // A temporarily holder for the converted ASCII values
        int shift; // A holder for the amount the text needs to be shifted
        int shiftIter = 0; // Key needs separate iterator so it can loop over

        // If it isn't an alphabetic letter, it keeps it unchanged
        for (int i = 0; i < charText.length; i++) {
            // Looping the key characters if text is longer
            if (i >= keyArray.length && shiftIter >= keyArray.length) shiftIter = 0;
            if (Character.isUpperCase(charText[i])) {
                // ASCII value of key (in int)
                shift = (int) keyArray[shiftIter];
                // -65 so it becomes its number in the alphabet
                shift -= 65;
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) - shift);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 90) ASCII -= 26;
                if (ASCII < 65) ASCII += 26;
                // ASCII value converted into its char representation and charText with index = i is updated
                charText[i] = (char) ASCII;
            }
            if (Character.isLowerCase(charText[i])) {
                // ASCII value of key (in int)
                shift = (int) keyArray[shiftIter];
                // -97 so it becomes its number in the alphabet
                shift -= 97;
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) - shift);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 122) ASCII -= 26;
                if (ASCII < 97) ASCII += 26;
                // ASCII value converted into its char representative and charText with index = i is updated
                charText[i] = (char) ASCII;
            }

            // Key's iterator is incremented
            shiftIter++;
        }

        // The finished charText is converted into a String and returned
        return new String(charText);
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
                Toast.makeText(VigenereCipher.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });
    }
}
