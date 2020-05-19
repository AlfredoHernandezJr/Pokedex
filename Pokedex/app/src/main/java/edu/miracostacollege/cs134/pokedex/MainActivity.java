package edu.miracostacollege.cs134.pokedex;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.IOException;
import java.net.URI;
import java.util.List;

import edu.miracostacollege.cs134.pokedex.model.DBHelper;
import edu.miracostacollege.cs134.pokedex.model.JSONLoader;
import edu.miracostacollege.cs134.pokedex.model.Pokemon;

public class MainActivity extends AppCompatActivity {

    private List<Pokemon> mPokedex;
    private DBHelper mDB;
    private ListView mPokemonListView;
    private PokemonListAdapter mPokemonListAdapter;

    private int additionalPokemonID = 152 ;

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DBHelper(this);
        mPokedex = mDB.getAllPokemon();

        if (mPokedex.size() == 0) {
            // DONE: After creating new method in JSONLoader to load the notusedpokedex.json
            // DONE: from the web (http), use the new method here.
            mPokedex = JSONLoader.loadJSONFromHTTP();
            for (Pokemon p : mPokedex)
                mDB.addPokemon(p);
        }

            mPokemonListAdapter = new PokemonListAdapter(this, R.layout.pokemon_list_item, mPokedex);
            mPokemonListView = findViewById(R.id.pokemonListView);
            mPokemonListView.setAdapter(mPokemonListAdapter);
    }

    // DONE: Implement the addPokemon method to create a new (relatively) empty Pokemon object
    // DONE: to the ListAdapter and the Database (DBHelper)
    public void addPokemon(View v)
    {
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);

        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Both name and description of the game must be provided.", Toast.LENGTH_LONG);
            return;
        }
        Pokemon newPokemon = new Pokemon(additionalPokemonID++, name, Uri.parse(""), new String[] {description},
                "", "", 0, "", 0.0, 0.0,
                "", new String[]{""} );

        // Add the new game to the database to ensure it is persisted.
        mDB.addPokemon(newPokemon);
        mPokemonListAdapter.add(newPokemon);
        // Clear all the entries (reset them)
        nameEditText.setText("");
        descriptionEditText.setText("");
    }

    // DONE: Implement the clearAllPokemon method to remove all Pokemon objects
    // DONE: from the ListAdapter and the Database (DBHelper)
    public void clearAllPokemon(View v)
    {
        mPokedex.clear();
        // Permanently delete pokemon from the database, buh bye
        mDB.deleteAllPokemon();
        mPokemonListAdapter.notifyDataSetChanged();
    }

    public void viewPokemonDetails(View v)
    {
        Pokemon selectedPokemon = (Pokemon) v.getTag();
        Intent detailsIntent = new Intent(this, PokemonDetailsActivity.class);
        detailsIntent.putExtra("SelectedPokemon", selectedPokemon);
        startActivity(detailsIntent);
    }
}
