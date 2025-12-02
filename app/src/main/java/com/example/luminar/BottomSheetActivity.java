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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
import model.*;

public class BottomSheetActivity extends BottomSheetDialogFragment implements View.OnClickListener {

    // Callback interface for notifying parent activity of changes
    public interface OnTaskChangeListener {
        void onTaskUpdated();
        void onTaskDeleted();
    }

    TextView txtTitle, txtDescription, txtStat, txtCategory, txtDueDate, txtFreq, txtStart, txtNext, txtEnd;
    EditText edDueDate, edStart, edEnd;
    Spinner spinStatus, spinFreq, spinCat;
    Button btnDel, btnUpdate;

    Switch notif;

    DatabaseReference catDB;

    private String taskId;
    private boolean isRecurring;
    private List<Category> allCategories = new ArrayList<>();
    private OnTaskChangeListener listener;

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

    // Callback for task change
    public void setOnTaskChangeListener(OnTaskChangeListener listener) {
        this.listener = listener;
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
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());

                    catDB.get().addOnSuccessListener(snapshot -> {
                        allCategories.clear();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Category c = ds.getValue(Category.class);
                            allCategories.add(c);
                        }

                        populateCategorySpinner(spinCat, allCategories, task.getCategory());
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
                txtDescription.setText(task.getDescription());
                txtCategory.setText(task.getCategory().getName());
                txtStat.setText(task.getStatus().toString());

                catDB.get().addOnSuccessListener(snapshot -> {
                    allCategories.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Category c = ds.getValue(Category.class);
                        allCategories.add(c);
                    }

                    populateCategorySpinner(spinCat, allCategories, task.getCategory());
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
            handleUpdate();
        }
        if(id==R.id.btnDelete){
            handleDelete();
        }
        if (id == R.id.edDueDate || id == R.id.edStartDate || id == R.id.edEndDate ) {
            ShowDatePicker(id);
        }

    }

    private void handleUpdate() {
        if (isRecurring) {
            RecurrentTask.load(taskId, task -> {
                if (task == null) {
                    Toast.makeText(getContext(), "Failed to load task", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update fields
                String selectedStatus = spinStatus.getSelectedItem().toString();
                task.setStatus(Status.valueOf(selectedStatus));

                String selectedFreq = spinFreq.getSelectedItem().toString();
                task.setFreq(Frequency.valueOf(selectedFreq));

                int catPosition = spinCat.getSelectedItemPosition();
                if (catPosition >= 0 && catPosition < allCategories.size()) {
                    task.setCategory(allCategories.get(catPosition));
                }

                task.setEnableNotif(notif.isChecked());
                task.setUpdatedAt(System.currentTimeMillis());

                // Parse dates
                try {
                    task.setStartCalendar(parseDateString(edStart.getText().toString()));
                    task.setNextOccurence(parseDateString(edEnd.getText().toString()));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save task
                task.save(task);
                Toast.makeText(getContext(), "Task updated successfully", Toast.LENGTH_SHORT).show();

                // Notify listener
                if (listener != null) {
                    listener.onTaskUpdated();
                }
                dismiss();
            });
        } else {
            NonRecurrentTask.load(taskId, task -> {
                if (task == null) {
                    Toast.makeText(getContext(), "Failed to load task", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update fields
                String selectedStatus = spinStatus.getSelectedItem().toString();
                task.setStatus(Status.valueOf(selectedStatus));

                int catPosition = spinCat.getSelectedItemPosition();
                if (catPosition >= 0 && catPosition < allCategories.size()) {
                    task.setCategory(allCategories.get(catPosition));
                }

                task.setEnableNotif(notif.isChecked());
                task.setUpdatedAt(System.currentTimeMillis());

                // Parse date
                try {
                    task.setDueDate(parseDateString(edDueDate.getText().toString()));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                    return;
                }

                task.save(task);
                Toast.makeText(getContext(), "Task updated successfully", Toast.LENGTH_SHORT).show();

                // Notify listener
                if (listener != null) {
                    listener.onTaskUpdated();
                }
                dismiss();
            });
        }
    }

    private void handleDelete() {
        if (isRecurring) {
            RecurrentTask task = new RecurrentTask();
            task.delete(taskId);
            Toast.makeText(getContext(), "Recurring task deleted", Toast.LENGTH_SHORT).show();
        } else {
            NonRecurrentTask task = new NonRecurrentTask();
            task.delete(taskId);
            Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
        }

        // Notify listener
        if (listener != null) {
            listener.onTaskDeleted();
        }
        dismiss();
    }

    private long parseDateString(String dateStr) {
        // DateConverter formats as "yyyy-MM-dd HH:mm:ss z"
        // DatePicker sets as "yyyy/M/d"
        // Function required to support both formats
        // and avoid errors in the future

        try {
            // Usage of slash = DatePicker
            if (dateStr.contains("/")) {
                String[] parts = dateStr.split("/");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1; // Calendar months are 0-based
                int day = Integer.parseInt(parts[2]);

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day, 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                return cal.getTimeInMillis();
            } else {
                // Re-parse
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault());
                Date date = sdf.parse(dateStr);
                return date != null ? date.getTime() : System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
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
