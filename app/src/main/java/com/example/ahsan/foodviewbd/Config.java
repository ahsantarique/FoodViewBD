package com.example.ahsan.foodviewbd;

/**
 * Created by Ahsan on 5/30/2016.
 */
public class Config {
    //URL to our login.php file
    public static final String SERVER = "http://192.168.43.70/";

    public static final String LOGIN_URL = SERVER+"test/volleyLogin.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "successful";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String USER_SHARED_PREF="user";


    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


    public static Restaurant SHARED_RESTAURANT =  new Restaurant();
    public static FoodItem SHARED_FOOD = new FoodItem();
}