package bigchicken.secretive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView ciphersView = (ListView) findViewById(R.id.cipher_list);
    String [] listNames = getResources().getStringArray(R.array.cipherNames);
    String [] listDesc = getResources().getStringArray(R.array.cipherDesc);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyListAdapter customAdapter = new MyListAdapter(this, listNames, listDesc);
        ciphersView.setAdapter(customAdapter);

        registerClickCallback(ciphersView);
    }

    private void registerClickCallback(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity.this, CaesarCipher.class));
                        break;
                }
            }
        });
    }
}
