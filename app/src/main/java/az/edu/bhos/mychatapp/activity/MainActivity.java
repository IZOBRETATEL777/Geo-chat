package az.edu.bhos.mychatapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import az.edu.bhos.mychatapp.R;
import az.edu.bhos.mychatapp.dao.Message;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_CODE = 1;
    private static final int REQUEST_CODE_SELECT_CITY = 101;
    private String currentLocation;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendBtn;
    private Toolbar toolbar;
    private PopupMenu popupMenu;
    private String currentName;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        currentLocation = sp.getString("location", "Baku");
        Log.i("MainActivity", "onCreate: " + currentLocation);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chat in " + currentLocation); // Set a custom title
        activity_main = findViewById(R.id.activity_main);
        sendBtn = findViewById(R.id.btnSend);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        sp.edit().putString("name", currentName).apply();
        sendBtn.setOnClickListener(view -> {
            EditText textField = findViewById(R.id.messageField);
            Message message = new Message(
                    currentName,
                    textField.getText().toString(),
                    currentLocation
            );
            if (textField.getText().toString().isEmpty()) {
                return;
            }
            FirebaseDatabase.getInstance().getReference().push().setValue(message);
            textField.setText("");

        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else {
            Snackbar.make(activity_main, "You are authorized", Snackbar.LENGTH_LONG).show();
            currentName = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
            displayAllMessages();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Show the hamburger icon
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24); // Set your custom icon
        }
        toolbar.setNavigationOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View view) {
        popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_open_maps) {
                Intent intent = new Intent(this, LocationSelectionActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_CITY);
                return true;
            }
            else if (item.getItemId() == R.id.action_open_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.action_open_current_location) {
                Intent intent = new Intent(this, CurrentLocationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_CITY);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (popupMenu != null) {
                popupMenu.dismiss(); // Close the popup menu when touching outside
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "You are authorized", Snackbar.LENGTH_LONG).show();
                displayAllMessages();
            }
            else {
                Snackbar.make(activity_main, "You are NOT authorized", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
        if (requestCode == REQUEST_CODE_SELECT_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                currentLocation = data.getStringExtra("selected_city");
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chat in " + currentLocation);
                displayAllMessages();
            }
        }
    }

    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListOptions.Builder<Message> builder = new FirebaseListOptions.Builder<>();
        builder
                .setLayout(R.layout.list_item)
                .setQuery(FirebaseDatabase.getInstance().getReference().orderByChild("location").equalTo(currentLocation), Message.class)
                .setLifecycleOwner(this);

        adapter = new FirebaseListAdapter<Message>(builder.build()) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getTextMessage());
                messageUser.setText(model.getUserName());
                messageTime.setText(DateFormat.format(
                        "HH:mm (dd MMM)",
                        model.getMessageTime()
                        ));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentName =  Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        displayAllMessages();
    }
}