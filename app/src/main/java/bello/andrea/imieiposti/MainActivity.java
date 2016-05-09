package bello.andrea.imieiposti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    private PlacesManager placesManager;
    private PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        placesManager = new PlacesManager(this);
        placesAdapter = new PlacesAdapter();

        ((ListView) findViewById(R.id.listview)).setAdapter(placesAdapter);

        final EditText editText = (EditText)findViewById(R.id.place_search_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateList(s.toString());
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("TITOLO");

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_layout, null);

                ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        Category.values()
                );
                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
                spinner.setAdapter(arrayAdapter);

                builder.setView(dialogView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Place place = new Place();
                        place.setName(((EditText) (dialogView.findViewById(R.id.place_name))).getText().toString());
                        place.setDescription(((EditText) (dialogView.findViewById(R.id.place_description))).getText().toString());
                        place.setLatitude(Double.parseDouble(((EditText) (dialogView.findViewById(R.id.place_latitude))).getText().toString()));
                        place.setLongitude(Double.parseDouble(((EditText) (dialogView.findViewById(R.id.place_longitude))).getText().toString()));
                        place.setCategory(Category.values()[spinner.getSelectedItemPosition()]);
                        placesManager.addPlace(place);
                        updateList(editText.getText().toString());
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });
    }

    private void updateList(){
        updateList(null);
    }

    private void updateList(String s){
        List<Place> places = placesManager.getAllPlaces(s);
        placesAdapter.reset();
        for (Place place : places) {
            placesAdapter.addPlace(place);
        }
        placesAdapter.notifyDataSetChanged();
    }

    private class PlacesAdapter extends BaseAdapter {

        private ArrayList<Place> places;

        public PlacesAdapter() {
            this.places = new ArrayList<>();
        }

        public void addPlace(Place place){
            places.add(place);
        }

        public void reset(){
            this.places = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return places.size();
        }

        @Override
        public Object getItem(int position) {
            return places.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.place_layout, null);
            }
            final Place place = places.get(position);
            TextView name = (TextView)convertView.findViewById(R.id.place_name);
            name.setText(place.getName());
            name.setTextColor(place.getCategory().getColor());
            ((TextView)convertView.findViewById(R.id.place_description)).setText(place.getDescription());
            convertView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placesManager.deletePlace(place);
                    places.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        placesManager.open();
        updateList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        placesManager.close();
    }
}
