package com.example.jelle.jellevannoord_pset4;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TodoAdapter extends ResourceCursorAdapter {

    public TodoAdapter(Context context, Cursor cursor, int flags) {
        super(context, R.layout.row_todo, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.todoName);
        String item = cursor.getString(cursor.getColumnIndex("title"));
        name.setText(item);

        TextView id = view.findViewById(R.id.itemID);
        id.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));

        CheckBox box = view.findViewById(R.id.checkBox);
        if(cursor.getInt(cursor.getColumnIndex("completed")) == 1) {
            box.setChecked(true);
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            name.setTextColor(MainActivity.resources.getColor(R.color.secondaryText));
        }
        box.setOnClickListener(new MainActivity.checkBoxListener());
    }
}