package rad_app.dicerollcounter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager {

    private SQLiteDatabase db;
    private static final String COLUMN_ID = "_id";
    private static final String ROLL = "roll";
    private static final String DIE = "die";
    private static final String CHARACTER = "character";
    private static final String TYPE = "rolltype";
    private static final String RESULT = "result";
    private static final String DATE = "date";
    private static final String DB_NAME = "dice_roll_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_D20 = "D20RollData";
    private static final String TABLE_DIE = "DieData";
    private static final String TABLE_CHARACTER = "CharacterData";


    public DataManager (Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void Insert(String roll, String die, String chara, String rolltype) { //only need one insert statement
        String query = "INSERT INTO " + TABLE_D20 + " (" +
                ROLL + ", " + DIE + ", " + CHARACTER + ", " + TYPE + ", " +
                RESULT + ", " + DATE + ") " +
                "VALUES (" + "'" + roll +  "'" + ", '" +
                die  +  "', '" + chara + "', '" + rolltype + "', 'Unknown', CURRENT_DATE);";

        Log.i("MYAPP",   query);
        db.execSQL(query);
    }

    public void InsertDie(String die){
        String query = "INSERT INTO " + TABLE_DIE + " (" +
                DIE + ") " + "VALUES (" + "'" + die + "');";
        db.execSQL(query);
    }

    public void InsertCharacter(String character){
        String query = "INSERT INTO " + TABLE_CHARACTER + " (" +
                CHARACTER + ") " + "VALUES (" + "'" + character + "');";
        db.execSQL(query);
    }

    public void Delete(String die, String chara){
        if(!die.equals("-1")){
            String query = "DELETE FROM " + TABLE_D20 +
                    " WHERE " + DIE + " = '" + die + "';";
            db.execSQL(query);
        }
        else{
            String query = "DELETE FROM " + TABLE_D20 +
                    " WHERE " + CHARACTER + " = '" + chara + "';";
            db.execSQL(query);
        }
    }

    public void DeleteDie(String die){
        String query = "DELETE FROM " + TABLE_DIE +
                " WHERE " + DIE + " = '" + die + "';";
        db.execSQL(query);
    }

    public void DeleteCharacter(String character){
        String query = "DELETE FROM " + TABLE_CHARACTER +
                " WHERE " + CHARACTER + " = '" + character + "';";
        db.execSQL(query);
    }

    //Update statement for deleting a type of die or character but keeping the data
    public void Update(String die, String chara) {

        String query;

        if(!die.equals("-1")) {
            query = "UPDATE " + TABLE_D20 + " SET " + DIE + " = '" + die +
                    "' WHERE " + DIE + " = '" + die + "';";
            Log.i("MYAPP", query);
        }
        else{
            query = "UPDATE " + TABLE_D20 + " SET " + CHARACTER + " = '" + chara +
                    "' WHERE " + CHARACTER + " = '" + chara + "';";
            Log.i("MYAPP", query);
        }
        db.execSQL(query);
    }

    public Cursor SelectAllDie(){
        Cursor c = db.rawQuery("SELECT * from " + TABLE_DIE + " ORDER BY " + DIE + " ASC;", null);
        return c;
    }

    public Cursor SelectAllCharacters(){
        Cursor c = db.rawQuery("SELECT *" + " from " + TABLE_CHARACTER + " ORDER BY " + CHARACTER + " ASC;", null);
        return c;
    }

    public Cursor SelectAll()
    {
        Cursor c = db.rawQuery("SELECT *" + " from " + TABLE_D20 + " ORDER BY " + ROLL + " ASC;", null);
        return c;
    }

    //A series of select statements depending on what is passed in
    public Cursor Search(String chara, String die, String rolltype) //Change to grab if everything is all except character
    {
        String query;
        query = "SELECT " + COLUMN_ID + ", " +
                ROLL + ", " + DIE
                + ", " + CHARACTER + ", " + TYPE + ", " + DATE + " from " +
                TABLE_D20 + " WHERE ";

        if(!chara.equals("All")){ //is a specific character selected?
            query += CHARACTER + " = '" + chara + "'";
        }
        if(!die.equals("All")){ //is a specific die selected?
            query += DIE + " = '" + die + "'";
        }
        if(!rolltype.equals("All")){ //is a specific type of roll selected?
            query += TYPE + " = '" + rolltype + "'";
        }

        query += ";"; //adds the semicolon

        Log.i("MYAPP", query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "create table "
                    + TABLE_D20 + " ("
                    + COLUMN_ID
                    + " integer primary key autoincrement not null,"
                    + ROLL
                    + " text not null,"
                    + DIE
                    + " text not null,"
                    + CHARACTER
                    + " text not null,"
                    + TYPE
                    + " text not null,"
                    + RESULT
                    + " text not null,"
                    + DATE
                    + " text not null);" ;
            db.execSQL(newTableQueryString);

            String newDieTableQueryString = "create table "
                    + TABLE_DIE + " ("
                    + COLUMN_ID
                    + " integer primary key autoincrement not null,"
                    + DIE
                    + " text not null);";
            db.execSQL(newDieTableQueryString);

            String newCharacterTableQueryString = "create table "
                    + TABLE_CHARACTER + " ("
                    + COLUMN_ID
                    + " integer primary key autoincrement not null,"
                    + CHARACTER
                    + " text not null);";
            db.execSQL(newCharacterTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

