package bigchicken.secretive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CaesarCipher extends AppCompatActivity {

    private String plainText;
    private String cipherText;
    private int inputKey;
    private boolean invalidKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caesar);
    }

    // Action when user clicks Encryption button
    public void encryptClicked(View view) {
        getData();
        if (invalidKey) return; // If entered key value is invalid, the method stops
        cipherText = encrypt(plainText, inputKey);

        // Changing layout to show output text
        setContentView(R.layout.output_layout);
    }

    // Action when user clicks Decryption button
    public void decryptClicked(View view) {
        getData();
        if (invalidKey) return; // If entered key value is invalid, the method stops
        inputKey *= -1; // inputKey is negated for decryption so same method can be used
        cipherText = encrypt(plainText, inputKey);

        // Changing layout to show output text
        setContentView(R.layout.output_layout);
    }

    // Get all text entered by user
    private void getData(){
        // What user entered into userInput and keyValue EditText is saved into local variables
        plainText = ((EditText) findViewById(R.id.userInput)).getText().toString();
        inputKey = Integer.parseInt(((EditText) findViewById(R.id.keyValue)).getText().toString());

        // TODO Experiment with input validation and error messages
        // Checking if key value is between 0-26
        if (inputKey > 26 || inputKey < 0){
            Toast.makeText(this, "Invalid Key Value", Toast.LENGTH_LONG).show();
            //((EditText) findViewById(R.id.keyValue)).setError("Invalid Key Value");
            invalidKey = true;
        }
        else invalidKey = false;
    }

    // Caesar Cipher Encryption method
    // For decryption, pass the negative equivalent of key value
    private String encrypt(String text, int key) {
        // Copying the String text into an array of chars so we can access it and modify it, index by index
        char[] charText = text.toCharArray();
        // A temporarily holder for the converted ASCII values
        int ASCII;

        // If it isn't an alphabetic letter, it keeps it unchanged
        for (int i = 0; i < charText.length; i++) {
            if (Character.isUpperCase(charText[i])) {
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) + key);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 90) ASCII -= 26;
                if (ASCII < 65) ASCII += 26;
                // ASCII value converted into its char representation and charText with index = i is updated
                charText[i] = (char) ASCII;
            }
            if (Character.isLowerCase(charText[i])) {
                // ASCII value of character (in int)
                ASCII = (((int) charText[i]) + key);
                // Checking if ASCII value after adding key would is still an alphabetic character
                // Adding/substracting 26 loops the value back to the right ASCII range
                if (ASCII > 122) ASCII -= 26;
                if (ASCII < 97) ASCII += 26;
                // ASCII value converted into its char representation and charText with index = i is updated
                charText[i] = (char) ASCII;
            }
        }

        // The finished charText is converted into a String and returned
        return new String(charText);
    }
}
