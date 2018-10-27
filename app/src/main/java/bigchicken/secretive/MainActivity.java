package bigchicken.secretive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView ciphersView = (ListView) findViewById(R.id.cipher_list);
        // Using MyListAdapter with parameters: (Activity context, String [] listNames, String [] listDesc)
        // listNames is the String array cipherNames from resources
        // listDec is the String array cipherDesc from resources
        ciphersView.setAdapter(new MyListAdapter(this,
                                getResources().getStringArray(R.array.cipherNames),
                                getResources().getStringArray(R.array.cipherDesc))
        );

        // Calling method to start activity depending on click
        registerClickCallback(ciphersView);
    }

    // Listener for click, on list item
    // Depending on the position of the item, a new activity is started
    // Position is determined by place in cipherNames String array in resources
    private void registerClickCallback(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity.this, CaesarCipher.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, VigenereCipher.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, PlayfairCipher.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, RailFenceCipher.class));
                        break;
                }
            }
        });
    }
}
