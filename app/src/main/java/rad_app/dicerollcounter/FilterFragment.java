package rad_app.dicerollcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.util.ArrayList;


public class FilterFragment extends Fragment {
    private View myView;
    private FilterInterface myCallbacks;

    ArrayList<String> die;
    ArrayList<String> pcs;

    Spinner dieSpinner;
    Spinner charSpinner;

    int SETTINGS = 1;

    public interface FilterInterface {
        void updateFilter(String value, String type);
    }

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_filter, container, false);

        //Initialize arrays
        die = new ArrayList<>();
        pcs = new ArrayList<>();

        Context context = myView.getContext();

        //Set up the spinners
        dieSpinner = myView.findViewById(R.id.die_spinner);
        dieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String die = adapterView.getSelectedItem().toString();
                myCallbacks.updateFilter(die, "die");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        charSpinner = myView.findViewById(R.id.character_spinner);
        charSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String character = adapterView.getSelectedItem().toString();
                myCallbacks.updateFilter(character, "character");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        Spinner typeSpinner = myView.findViewById(R.id.rolltype_spinner);
        ArrayAdapter<CharSequence> adapterjr = ArrayAdapter.createFromResource(context, R.array.roll_type,
                android.R.layout.simple_spinner_item);
        adapterjr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapterjr);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = adapterView.getSelectedItem().toString();
                myCallbacks.updateFilter(type, "type");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        //Initialize button
        ImageButton settings = myView.findViewById(R.id.imbtnSettings);
        settings.setOnClickListener(settingsListener);

        // Inflate the layout for this fragment
        return myView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FilterInterface) {
            myCallbacks = (FilterInterface) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement FilterInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myCallbacks = null;
    }

    //On click for settings button
    private View.OnClickListener settingsListener = new View.OnClickListener() {
        public void onClick(View v) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, Settings.class);

            getActivity().startActivityForResult(intent, SETTINGS);
        }
    };

    //Called from main when arrays are updated
    public void updateSpinners(ArrayList<String> Die, ArrayList<String> characters){
        die = Die;
        pcs = characters;

        Context context = myView.getContext();
        ArrayAdapter<String> adapter =  new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, die);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dieSpinner.setAdapter(adapter);

        adapter =  new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, pcs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        charSpinner.setAdapter(adapter);
    }
}
