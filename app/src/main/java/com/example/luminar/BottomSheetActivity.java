package com.example.luminar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import model.DateConverter;
import model.NonRecurrentTask;
import model.RecurrentTask;

public class BottomSheetActivity extends BottomSheetDialogFragment {

    TextView txtTitle, txtDescription, txtCategory, txtDueDate, txtFreq, txtStart, txtNext;

    Spinner spinStatus;

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

        txtTitle = view.findViewById(R.id.tvTitle);
        txtDescription = view.findViewById(R.id.txtDesc);
        txtCategory = view.findViewById(R.id.txtCat);
        spinStatus = view.findViewById(R.id.spinStat);
        txtDueDate = view.findViewById(R.id.txtDueDate);
        txtFreq = view.findViewById(R.id.txtFreq);
        txtStart = view.findViewById(R.id.txtStartDate);
        txtNext = view.findViewById(R.id.txtNextOcurrence);

        if (isRecurring) {

            RecurrentTask.load(taskId, task -> {
                if (task == null) return;

                txtFreq.setVisibility(View.VISIBLE);
                txtStart.setVisibility(View.VISIBLE);
                txtNext.setVisibility(View.VISIBLE);
                txtDueDate.setVisibility(View.GONE);

                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().toString());
                spinStatus.getSelectedItem();

                txtFreq.setText(task.getFreq().toString());
                txtStart.setText(DateConverter.convertMillisToFormattedDate(task.getStartCalendar()));
                txtNext.setText(DateConverter.convertMillisToFormattedDate(task.getNextOccurence()));
            });

        } else {

            NonRecurrentTask.load(taskId, task -> {
                if (task == null) return;

                txtFreq.setVisibility(View.GONE);
                txtStart.setVisibility(View.GONE);
                txtNext.setVisibility(View.GONE);
                txtDueDate.setVisibility(View.VISIBLE);

                txtTitle.setText(task.getTitle());
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().toString());
                txtStatus.setText(task.getStatus().toString());
                txtDueDate.setText(DateConverter.convertMillisToFormattedDate(task.getDueDate()));
            });
        }
    }
}
