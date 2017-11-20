package com.example.jelle.jellevannoord_pset4;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static Resources resources;
    private static TodoDatabase db;
    private static TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources =  getResources();

        db = TodoDatabase.getInstance(getApplicationContext());
        adapter = new TodoAdapter(getApplicationContext(), db.selectAll(), 0);
        ListView listView = findViewById(R.id.todoList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new listClickListener());
        listView.setOnItemLongClickListener(new listLongClickListener());
    }

    public void addItem(View view) {
        EditText todoItem = findViewById(R.id.userInput);
        String itemName = todoItem.getText().toString();
        if(itemName.matches("")) {
            Snackbar.make(findViewById(R.id.constraintLayout),"No item was entered", Snackbar.LENGTH_LONG).show();
        } else {
            db.insert(itemName, 0);
            todoItem.setText("");
            hideKeyboard(MainActivity.this);
            updateData();
        }
    }

    public static void updateData() {
        adapter.swapCursor(db.selectAll());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void handleItemStateChange(LinearLayout linearLayout, boolean boxChange) {
        TextView itemId = (TextView) linearLayout.getChildAt(1);
        CheckBox box = (CheckBox) linearLayout.getChildAt(0);
        TextView name = (TextView) linearLayout.getChildAt(2);
        if(boxChange) {
            if(box.isChecked()) {
                box.setChecked(false);
            } else {
                box.setChecked(true);
            }
        }
        int completed;
        if(box.isChecked()) {
            completed = 1;
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setTextColor(resources.getColor(R.color.secondaryText));
        } else {
            completed = 0;
            name.setPaintFlags(name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            name.setTextColor(resources.getColor(R.color.primaryText));
        }
        db.update(itemId.getText().toString(), completed);
        updateData();
    }

    private class listClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            LinearLayout linearLayout = (LinearLayout) view;
            handleItemStateChange(linearLayout, true);
        }
    }

    private class listLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            final LinearLayout linearLayout = (LinearLayout) view;

            return true;
        }
    }

    public static class checkBoxListener implements CheckBox.OnClickListener {
        @Override
        public void onClick(View view) {
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            handleItemStateChange(linearLayout, false);
        }
    }
}
