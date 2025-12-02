package com.example.luminar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
import model.*;

public class BottomSheetActivity extends BottomSheetDialogFragment implements View.OnClickListener {

    TextView txtTitle, txtStat, txtCategory, txtDueDate, txtFreq, txtStart, txtEnd;
    EditText edDueDate, edStart, edEnd, edDesc;
    Spinner spinStatus, spinFreq, spinCat;
    Button btnDel, btnUpdate;

    Switch notif;

    DatabaseReference catDB;

    private String taskId;
    private boolean isRecurring;

    public static BottomSheetActivity newInstance(String taskId, boolean isRecurring) {
        BottomSheetActivity sheet = new BottomSheetActivity();
        Bundle args = new Bundle();
        args.putString("taskId", taskId);
        args.putBoolean("type", isRecurring);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.activity_bottom_sheet, container, false);

        if (getArguments() != null) {
            taskId = getArguments().getString("taskId");
            isRecurring = getArguments().getBoolean("type", false);
        }

        initialize(view);
        return view;
    }

    public void initialize(View view) {

        // UI Elements
        txtTitle = view.findViewById(R.id.tvTitle);
        edDesc = view.findViewById(R.id.txtDesc);

        txtCategory = view.findViewById(R.id.tvCategory);
        spinCat = view.findViewById(R.id.spinCat);

        txtStat = view.findViewById(R.id.tvStatus);
        spinStatus = view.findViewById(R.id.spinStat);

        txtDueDate = view.findViewById(R.id.tvDueDate);
        edDueDate = view.findViewById(R.id.edDueDate);
        edDueDate.setOnClickListener(this);

        txtFreq = view.findViewById(R.id.tvFreq);
        spinFreq = view.findViewById(R.id.spinFreq);

        txtStart = view.findViewById(R.id.tvStartDate);
        edStart = view.findViewById(R.id.edStartDate);
        edStart.setOnClickListener(this);

        txtEnd = view.findViewById(R.id.tvEndDate);
        edEnd = view.findViewById(R.id.edEndDate);
        edEnd.setOnClickListener(this);

        catDB = FirebaseDatabase.getInstance().getReference("categories");
        //Nofitication dunno how to implement it
        notif = view.findViewById(R.id.toggleNotif);
        btnUpdate =  view.findViewById(R.id.btnUpdate);
        btnDel =  view.findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


        // Load task data
        if (isRecurring) {

            RecurrentTask.load(taskId, task -> {
                if (task == null) return;

                // Show recurring fields
                txtFreq.setVisibility(View.VISIBLE);
                spinFreq.setVisibility(View.VISIBLE);

                txtStart.setVisibility(View.VISIBLE);
                edStart.setVisibility(View.VISIBLE);

                txtEnd.setVisibility(View.VISIBLE);
                edEnd.setVisibility(View.VISIBLE);

                txtDueDate.setVisibility(View.GONE);
                edDueDate.setVisibility(View.GONE);

                // Set values
                txtTitle.setText(task.getTitle());
                edDesc.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());

                    catDB.get().addOnSuccessListener(snapshot -> {
                        List<Category> allCats = new ArrayList<>();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Category c = ds.getValue(Category.class);
                            allCats.add(c);
                        }

                        populateCategorySpinner(spinCat, allCats, task.getCategory());
                    });

                populateSpinner(spinStatus, Status.class, task.getStatus());
                // Frequency spinner
                populateSpinner(spinFreq, Frequency.class, task.getFreq());

                edStart.setText(DateConverter.convertMillisToFormattedDate(task.getStartCalendar()));
                edEnd.setText(DateConverter.convertMillisToFormattedDate(task.getNextOccurence()));
            });

        } else {

            NonRecurrentTask.load(taskId, task -> {
                if (task == null) return;

                // Hide recurring fields
                txtFreq.setVisibility(View.GONE);
                spinFreq.setVisibility(View.GONE);

                txtStart.setVisibility(View.GONE);
                edStart.setVisibility(View.GONE);

                txtEnd.setVisibility(View.GONE);
                edEnd.setVisibility(View.GONE);

                txtDueDate.setVisibility(View.VISIBLE);
                edDueDate.setVisibility(View.VISIBLE);

                txtTitle.setText(task.getTitle());
                edDesc.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());
                txtStat.setText(task.getStatus().toString());

                catDB.get().addOnSuccessListener(snapshot -> {
                    List<Category> allCats = new ArrayList<>();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Category c = ds.getValue(Category.class);
                        allCats.add(c);
                    }

                    populateCategorySpinner(spinCat, allCats, task.getCategory());
                });


                // Populate Status spinner and select correct status
                populateSpinner(spinStatus, Status.class, task.getStatus());

                // Set date
                edDueDate.setText(DateConverter.convertMillisToFormattedDate(task.getDueDate()));
            });
        }
    }


    private <T extends Enum<T>> void populateSpinner(Spinner spinner, Class<T> enumClass, Enum<?> selectedValue) {

        // Convert enum values to string list
        T[] values = enumClass.getEnumConstants();
        List<String> items = new ArrayList<>();

        for (T value : values) {
            items.add(value.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                items
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Select correct value
        if (selectedValue != null) {
            int index = adapter.getPosition(selectedValue.toString());
            if (index >= 0) {
                spinner.setSelection(index);
            }
        }
    }

    private void populateCategorySpinner(Spinner spinner, List<Category> list, Category selected) {

        List<String> names = new ArrayList<>();
        for (Category c : list) {
            names.add(c.getName()); // what the spinner will show
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                names
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Pre-select current task category
        if (selected != null) {
            int index = names.indexOf(selected.getName());
            if (index >= 0) {
                spinner.setSelection(index);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btnUpdate){
        }
        if(id==R.id.btnDelete){
        }
        if (id == R.id.edDueDate || id == R.id.edStartDate || id == R.id.edEndDate ) {
            ShowDatePicker(id);
        }

    }

    private void ShowDatePicker(int viewId) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    selectedMonth++; // because DatePicker months are 0-based
                    String dateString = selectedYear + "/" + selectedMonth + "/" + selectedDay;

                    if (viewId == R.id.edDueDate) {
                        edDueDate.setText(dateString);
                    }
                    else if (viewId == R.id.edStartDate) {
                        edStart.setText(dateString);
                    }
                    else if (viewId == R.id.edEndDate) {
                        edEnd.setText(dateString);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

}
