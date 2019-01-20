package rad_app.dicerollcounter;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CounterFragment extends Fragment {

    private View myView;

    public CounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_counter, container, false);  // Inflate the layout for this fragment

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateViews(int[] rolls){
        String id = "tvNat";
        String temp;
        int resID;
        TextView tv;

        //Loop through the array and update the textviews
        for(int i = 0; i < rolls.length; i++){
            temp = id + Integer.toString(i + 1); //build the name of the textview that needs to be updated
            resID = getResources().getIdentifier(temp, "id", getActivity().getPackageName()); //get the id of the textview
            tv = myView.findViewById(resID); //assign the textview to tv
            tv.setText(Integer.toString(rolls[i])); //assign the value to tv
        }
    }
}
