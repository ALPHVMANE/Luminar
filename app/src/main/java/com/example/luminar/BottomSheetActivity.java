package com.example.luminar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.*;
import model.*;

public class BottomSheetActivity extends BottomSheetDialogFragment {

    TextView txtTitle, txtDescription, txtStat, txtCategory, txtDueDate, txtFreq, txtStart, txtNext, txtEnd;
    EditText edDueDate, edStart, edEnd;
    Spinner spinStatus, spinFreq, spinCat;
    Button btnDel, btnUpdate;

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
        txtDescription = view.findViewById(R.id.txtDesc);

        txtCategory = view.findViewById(R.id.tvCategory);
        spinCat = view.findViewById(R.id.spinCat);

        txtStat = view.findViewById(R.id.tvStatus);
        spinStatus = view.findViewById(R.id.spinStat);

        txtDueDate = view.findViewById(R.id.tvDueDate);
        edDueDate = view.findViewById(R.id.edDueDate);

        txtFreq = view.findViewById(R.id.tvFreq);
        spinFreq = view.findViewById(R.id.spinFreq);

        txtStart = view.findViewById(R.id.tvStartDate);
        edStart = view.findViewById(R.id.edStartDate);

        txtEnd = view.findViewById(R.id.tvEndDate);
        edEnd = view.findViewById(R.id.edEndDate);

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
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());

                txtFreq.setText(task.getFreq().toString());
                txtStart.setText(DateConverter.convertMillisToFormattedDate(task.getStartCalendar()));
                txtNext.setText(DateConverter.convertMillisToFormattedDate(task.getNextOccurence()));
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

                // Set values
                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());
                txtStat.setText(task.getStatus().toString());

                txtDueDate.setText(DateConverter.convertMillisToFormattedDate(task.getDueDate()));
            });
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

}
