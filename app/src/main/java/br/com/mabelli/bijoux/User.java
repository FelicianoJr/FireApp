package br.com.mabelli.bijoux;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by feliciano on 26/06/17.
 */
@IgnoreExtraProperties
public class User {

    public String email;
    public String name;
    public String token;

    public User() {
    }

    public User(String email, String name, String token) {
        this.email = email;
        this.name = name;
        this.token = token;
    }


}
