package services;

import com.google.firebase.auth.FirebaseAuth;

public class AuthServices {
    private final FirebaseAuth mAuth;

    public AuthServices(){
        this.mAuth = FirebaseAuth.getInstance();
    }
}
