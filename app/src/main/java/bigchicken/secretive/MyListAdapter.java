package bigchicken.secretive;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MyListAdapter extends ArrayAdapter {

    private String [] listNames;
    private String [] listDesc;
    private Activity context;

    public MyListAdapter(Activity context, String [] listNames, String [] listDesc) {
        super(context, R.layout.listview_layout, listNames);
        this.context = context;
        this.listNames = listNames;
        this.listDesc = listDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            itemView = layoutInflater.inflate(R.layout.listview_layout, parent, false);
        }

        TextView cipherName = (TextView)itemView.findViewById(R.id.cipher_name);
        cipherName.setText(listNames[position]);

        TextView cipherDesc = (TextView)itemView.findViewById(R.id.cipher_description);
        cipherDesc.setText(listDesc[position]);

        return itemView;
    }
}
