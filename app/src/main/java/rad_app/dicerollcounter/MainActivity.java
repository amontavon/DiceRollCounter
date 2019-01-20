package rad_app.dicerollcounter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements UpdateFragment.UpdateInterface, FilterFragment.FilterInterface{

    DataManager db; //connects to database

    //hold values of filter drop downs
    String filter_die;
    String filter_character;
    String filter_type;

    //hold all dies/characters in database
    ArrayList<String> dies;
    ArrayList<String> pcs;

    //Local versions of fragments
    CounterFragment counter;
    FilterFragment filter;
    UpdateFragment update;

    int sides = 20; //currently this is static because this app is only for d20 rolls
    int[] rollcount; //array of rolls for the counter fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataManager(this); //initialize db manager

        //initialize filters
        filter_die = "All";
        filter_character = "All";
        filter_type = "All";

        //initialize dies and characters
        dies = new ArrayList<>();
        pcs = new ArrayList<>();

        updateData(); //update arrays with values from the database

        //Create the filter fragment
        FragmentManager fragmentManager = getFragmentManager();
        if( fragmentManager.findFragmentById(R.id.filter_fragment) == null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FilterFragment fragment = new FilterFragment();
            transaction.add(R.id.filter_fragment, fragment);
            transaction.commit();
        }

        //Create the counter fragment
        if( fragmentManager.findFragmentById( R.id.counter_fragment ) == null ) {
            FragmentTransaction transaction = fragmentManager.beginTransaction( );
            CounterFragment fragment = new CounterFragment( );
            transaction.add(R.id.counter_fragment, fragment);
            transaction.commit( );
        }

        //Create the question fragment
        if( fragmentManager.findFragmentById( R.id.update_fragment ) == null ) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            UpdateFragment fragment = new UpdateFragment();
            transaction.add(R.id.update_fragment, fragment);
            transaction.commit();
        }

        //initalize the roll array
        rollcount = new int[sides];
        updateArray();

        counter = (CounterFragment) getFragmentManager().findFragmentById(R.id.counter_fragment);
        //initialize views
        counter.updateViews(rollcount);

        //initialize drop downs in filter fragment
        filter = (FilterFragment) getFragmentManager().findFragmentById(R.id.filter_fragment);
        filter.updateSpinners(dies, pcs);

        //initialize drop downs in update fragment
        update = (UpdateFragment) getFragmentManager().findFragmentById(R.id.update_fragment);
        update.updateSpinners(dies, pcs);
    }

    //updates database with new rolled value, update the array, and then update the views
    public void processData(String roll, String die, String character, String type){
        //Insert data
        db.Insert(roll, die, character, type);

        //Update the array
        updateArray();

        //Update the views
        counter.updateViews(rollcount);
    }

    //Function that updates the array based on what filters are selected
    private void updateArray(){

        Cursor c;

        //If filters are all set to all, do a select *
        if(filter_die.equals("All") && filter_character.equals("All") && filter_type.equals("All")){
            c = db.SelectAll();
        }
        else{ //else, pull a select based on the filter values
            c = db.Search(filter_character, filter_die, filter_type);
        }

        //initialize array
        for (int i = 0; i < rollcount.length; i++) {
            rollcount[i] = 0;
        }

        //if the cursor isn't empty, fill the array with values to pass to counter fragment
        if(c.getCount() != 0) {

            //loop through cursor
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToNext();
                rollcount[Integer.parseInt(c.getString(1)) - 1]++;
            }
        }
    }

    //update the counter view based on the filters
    public void updateFilter(String value, String type){
        if(type.equals("die")){ //update die filter
            filter_die = value;
        }
        else if(type.equals("character")){ //update character filter
            filter_character = value;
        }
        else if(type.equals("type")){ //update type
            filter_type = value;
        }

        //update the array with the new filter and reset the view
        updateArray();
        counter.updateViews(rollcount);
    }

    //Update the die and character arrays with values from the database
    public void updateData(){

        Cursor d = db.SelectAllDie();
        Cursor c = db.SelectAllCharacters();

        if(d.getCount() > 0) { //if the die cursor is not empty
            dies.clear(); //clear what's there
            dies.add("All"); //Add "All" value to the list
            for (int i = 0; i < d.getCount(); i++) { //add values to the array list from the cursor
                d.moveToNext();
                dies.add(d.getString(1));
            }
        }
        if(c.getCount() > 0){ //if the character cursor is not empty
            pcs.clear(); //clear the array list
            pcs.add("All"); //Add "All" value to the list
            for(int i = 0; i < c.getCount(); i++){ //add values to the array list from the cursor
                c.moveToNext();
                pcs.add(c.getString(1));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){ //if something was added or deleted

            updateData(); //update the array lists
            filter.updateSpinners(dies, pcs); //update the spinners in the filter fragment

            update.updateSpinners(dies, pcs); //update the spinners in the update fragment
        }
    }
}
