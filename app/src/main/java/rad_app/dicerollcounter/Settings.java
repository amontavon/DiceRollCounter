package rad_app.dicerollcounter;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    DataManager db; //db connection

    //ArrayLists for dies and characters
    ArrayList<String> dies;
    ArrayList<String> pcs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //set up the die or character selection spinner
        Spinner adddeleteSpinner = findViewById(R.id.object_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.add_delete_list,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adddeleteSpinner.setAdapter(adapter);

        db = new DataManager(this); //initialize database manager
        dies = new ArrayList<>(); //initialize array list for dies
        pcs = new ArrayList<>(); //initialize array list for characters

        updateArrayLists(); //update array values
    }

    //onClick for the back button
    public void onBack(View v){
        setResult(RESULT_CANCELED);
        this.finish();
    }

    //onClick for the submit button
    public void onSubmit(View v){
        Spinner spChoice = findViewById(R.id.object_spinner); //character or die selection

        RadioGroup rgAddDelete = findViewById(R.id.rgAddDelete);
        if(rgAddDelete.getCheckedRadioButtonId() == R.id.rbAdd){ //they are trying to add a value
            EditText et = findViewById(R.id.etName);
            String temp = et.getText().toString();

            if(spChoice.getSelectedItem().toString().equals("Die")){ //they are trying to add a die
                if(!temp.equals("") && !temp.equals("All")) { //if the EditText isn't empty or equal to "All"
                    db.InsertDie(temp); //insert the value into the die table
                }
            }
            else{
                if(!temp.equals("") && !temp.equals("All")){ //if the EditText isn't empty or equal to "All"
                    db.InsertCharacter(temp); //insert the value into the character table
                }
            }
        }
        else{ //else the delete button is selected
            RadioGroup rgData = findViewById(R.id.rgData);
            Spinner spList = findViewById(R.id.delete_spinner);

            if(!spChoice.getSelectedItem().toString().equals("All")) { //if they aren't trying to delete the "All" value

                if (spChoice.getSelectedItem().toString().equals("Die")) { //if they are trying to delete a die
                    db.DeleteDie(spList.getSelectedItem().toString()); //delete it from the database

                    if (rgData.getCheckedRadioButtonId() == R.id.rbDeleteData) { //if they didn't want to keep data
                        db.Delete(spList.getSelectedItem().toString(), "-1"); //delete everything related to this die
                    } else { //if they did want to keep data
                        db.Update("All", "-1"); //change the die value to all
                    }
                } else { //else they are trying to delete a character
                    db.DeleteCharacter(spList.getSelectedItem().toString()); //delete the selected character from db

                    if (rgData.getCheckedRadioButtonId() == R.id.rbDeleteData) { //if they did not want to keep data
                        db.Delete("-1", spList.getSelectedItem().toString()); //then delete associated data from the roll table
                    } else { //else they wanted to keep associated table
                        db.Update("-1", "All"); //change related data in roll table to "All"
                    }
                }
            }
        }

        setResult(RESULT_OK); //Things are good! There was a change
        this.finish();
    }

    //Update fields depending on what radio button is selected
    public void onActionChange(View v){
        TextView tvName = findViewById( R.id.tvName);
        EditText etName = findViewById(R.id.etName);
        Spinner spChoice = findViewById(R.id.object_spinner);
        Spinner spSelect = findViewById(R.id.delete_spinner);
        TextView tvSelect = findViewById(R.id.tvSelect);
        TextView tvDelete = findViewById(R.id.tvIWouldDelete);
        RadioGroup rgDelete = findViewById(R.id.rgData);

        switch(v.getId()){
            case R.id.rbAdd: //they are adding a value

                //Update field visibilities
                tvSelect.setVisibility(View.INVISIBLE);
                spSelect.setVisibility(View.INVISIBLE);
                tvDelete.setVisibility(View.INVISIBLE);
                rgDelete.setVisibility(View.INVISIBLE);
                tvName.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);

                if(spChoice.getSelectedItem().toString().equals("Die")){ //if they are adding a die
                    tvName.setText(getString(R.string.name_die));
                }
                else{ //otherwise they are adding a character
                    tvName.setText(getString(R.string.name_character));
                }
                break;

            case R.id.rbDelete: //they are going to delete a value

                //Update field visibilities
                tvSelect.setVisibility(View.VISIBLE);
                spSelect.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
                rgDelete.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.INVISIBLE);
                etName.setVisibility(View.INVISIBLE);

                if(spChoice.getSelectedItem().toString().equals("Die")){ //setup for deleting a die
                    tvSelect.setText(getString(R.string.select));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, dies);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSelect.setAdapter(adapter);
                }
                else{ //setup for deleting a character
                    tvSelect.setText(getString(R.string.select_character));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, pcs);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSelect.setAdapter(adapter);
                }

                break;
        }
    }

    //update the spinner for dies/characters upon starting activity
    private void updateArrayLists(){
        Cursor d = db.SelectAllDie();
        Cursor c = db.SelectAllCharacters();

        if(d.getCount() > 0) { //if the die cursor is not empty
            dies.clear(); //clear what's there
            for (int i = 0; i < d.getCount(); i++) { //add values to the array list from the cursor
                d.moveToNext();
                dies.add(d.getString(1));
            }
        }
        if(c.getCount() > 0){ //if the character cursor is not empty
            pcs.clear(); //clear the array list
            for(int i = 0; i < c.getCount(); i++){ //add values to the array list from the cursor
                c.moveToNext();
                pcs.add(c.getString(1));
            }
        }
    }
}
