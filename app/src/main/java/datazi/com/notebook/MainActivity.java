package datazi.com.notebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.note) {
            Intent intent = new Intent(MainActivity.this, NoteEditor.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* //add item to arraylist
        notes.add("Example Notes");
        //add arrayList
      */
       ListView listView = findViewById(R.id.listView);
        //shared preferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("datazi.com.notebook", Context.MODE_PRIVATE);
        HashSet<String> hashSet=(HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if (hashSet == null){

            notes.add("Example notes");

        }
        else
        {
            notes=new ArrayList(hashSet);
        }


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEditor.class);
                //here local var i hold row id on whichbutton tapped
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);//delete notes based on id
                                arrayAdapter.notifyDataSetChanged();

                                //shared prefererneces
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("datazi.com.notebook", Context.MODE_PRIVATE);
                                //create String set
                                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);

                                sharedPreferences.edit().putStringSet("notes", hashSet).apply();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
