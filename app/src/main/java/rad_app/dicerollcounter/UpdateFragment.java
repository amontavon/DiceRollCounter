package rad_app.dicerollcounter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;


public class UpdateFragment extends Fragment {
    private View myView;
    private UpdateInterface myCallbacks;

    ArrayList<String> die;
    ArrayList<String> pcs;

    Spinner dieSpinner;
    Spinner charSpinner;

    public interface UpdateInterface {
        void processData(String roll, String die, String character, String type);
    }

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_update, container, false);

        //Initialize array lists
        die = new ArrayList<>();
        pcs = new ArrayList<>();

        //Populate the spinners
        Spinner d20Spinner = myView.findViewById(R.id.d20_spinner);
        Context context = myView.getContext();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.d20_sides, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d20Spinner.setAdapter(adapter);

        dieSpinner = myView.findViewById(R.id.die_spinner);

        charSpinner = myView.findViewById(R.id.character_spinner);

        Spinner typeSpinner = myView.findViewById(R.id.rolltype_spinner);
        adapter = ArrayAdapter.createFromResource(context, R.array.roll_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Inflate the layout for this fragment
        return myView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UpdateInterface) {
            myCallbacks = (UpdateInterface) activity;
        } else {
            throw new RuntimeException(activity.toString() + "must implement Callbacks");
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        //initialize on click for roll button
        Button btnRoll = myView.findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(btnListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myCallbacks = null;
    }

    //on click for roll button
    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            //What's selected?
            Spinner d20Spinner = myView.findViewById(R.id.d20_spinner);
            String roll = d20Spinner.getSelectedItem().toString();

            String die = dieSpinner.getSelectedItem().toString();

            String character = charSpinner.getSelectedItem().toString();

            Spinner typeSpinner = myView.findViewById(R.id.rolltype_spinner);
            String type = typeSpinner.getSelectedItem().toString();

            //pass data to main to update db and views
            myCallbacks.processData(roll, die, character, type);
        }
    };

    //Called from main when the arrays are updated
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
