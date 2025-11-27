package model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import model.Global;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

public class User{
    private String id;
    private String name;
    private String email;
    private String profilePicURL; //TO DO
    private String FCMToken;

    private boolean enableNotif;

//    private Role role; (admin feature) TBD

    //constructors
    public User(String id, String name, String email, boolean enableNotif) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enableNotif = enableNotif;
    }
    //need empty required for firebase
    public User(){

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnableNotif() {
        return enableNotif;
    }

    public void setEnableNotif(boolean enableNotif) {
        this.enableNotif = enableNotif;
    }


    //methods


    //returns current user Id String on login
    // from activity replace context by 'this'(like this.id but put only 'this')
    public void login(Context context, String email){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot child : snapshot.getChildren()) {
                        //Store global variable
                        String userId = child.getKey();
                        Global app = (Global) context.getApplicationContext();
                        Global.setUid(userId);
                        Log.d("Firebase", "User ID: " + userId);
                        break;
                    }
                }
                else{
                    Log.d("Firebase", "No user found with email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }

    //delete a user
    public void delete(String uid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.child(uid).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "User deleted successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to delete user: " + e.getMessage()));
    }

    //save a user
    public void save(User user){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.child(user.getId()).setValue(user)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "User saved successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to save user: " + e.getMessage()));
    }

    // a bit like IOS it safely checks if there is a user before and then returns a user or a failure
    public void loadUser(String uid, Consumer<User> onResult){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                onResult.accept(user);
            }

    //            @Override
    //            public void onCancelled(@NonNull DatabaseError error) {
    //                onResult.accept(null);
    //            }
    //        });
    //    }
    //
    //    // then load the user like this :

    //loadUserByUid(Global.getUid(), user -> {
    //    if (user != null) {
    //        Log.d("Firebase", "Loaded user: " + user.getName());
    //    } else {
    //        Log.e("Firebase", "User not found!");
    //    }
    //});

}
