package com.example.schemaPhase2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.example.schemaPhase2.Model.ToDoModel;
import com.example.schemaPhase2.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText,newTaskDate,newTaskTime;
    private Button newTaskSaveButton,newTaskButton, newTaskTimeButton;
    private DatabaseHandler db;
public int mYear,mMonth,mDay, mHour,mMinute;
    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
newTaskButton = getView().findViewById(R.id.TaskBtn);
       /* newTaskDate = getView().findViewById(R.id.newTaskDate);*/
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newTaskButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
      /*  newTaskTime = getView().findViewById(R.id.newTaskTime);*/
newTaskTimeButton = getView().findViewById(R.id.newTaskTimeButton);
newTaskTimeButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                newTaskTimeButton.setText(hourOfDay + ":" + minute);
            }
        },mHour,mMinute,false);
        timePickerDialog.show();
    }
});
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null){
            isUpdate = true;
            String task = bundle.getString("task");
            String taskTime = bundle.getString("taskTime");
            String taskDate = bundle.getString("taskDate");
            newTaskText.setText(task);
            newTaskButton.setText(taskDate);
            newTaskTimeButton.setText(taskTime);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.purple_700));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.purple_700));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                String textDate = newTaskButton.getText().toString();
                String textTime = newTaskTimeButton.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text,textDate,textTime);

                }
                else{
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);

                    task.setTaskDate(textDate);
                    task.setTaskTime(textTime);

                    /* ToDoModel taskDate = new ToDoModel();
                    taskDate.setTaskDate(textDate);

                    ToDoModel taskTime = new ToDoModel();
                    taskTime.setTaskTime(textTime);
*/
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }

    }
}
