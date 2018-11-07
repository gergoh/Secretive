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

public class PlayfairCipher extends AppCompatActivity {

    private String plainText;
    private String cipherText;
    private String inputKey;

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
        getData(plainText, cipherText);
        startOutput(cipherText);
    }

    // Action when user clicks Decryption button
    public void decryptClicked(View view) {
        getData(cipherText, plainText);
        startOutput(plainText);
    }

    // Get all text entered by user
    private void getData(String inputText, String outputText){
        // What user entered into userInput and keyValue EditText is saved into local variables
        inputText = ((EditText) findViewById(R.id.userInput)).getText().toString();
        inputKey = ((EditText) findViewById(R.id.keyValue)).getText().toString();
        outputText = encrypt(inputText, inputKey);
    }

    // Playfair Cipher Encryption method
    private String encrypt(String text, String key) {
    }

    // Playfair Cipher Decryption method
    private String decrypt(String text, String key) {
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
