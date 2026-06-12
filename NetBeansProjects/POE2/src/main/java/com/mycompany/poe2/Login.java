/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poe2;

import java.util.regex.Pattern;

public class Login {
    private String savedUser, savedPass, fName, lName;

    public boolean checkUserName(String user) {
        return user.contains("_") && user.length() <= 5;
    }

    public boolean checkPasswordComplexity(String pass) {
        return pass.length() >= 8 &&
               pass.matches(".*[A-Z].*") &&
               pass.matches(".*[0-9].*") &&
               pass.matches(".*[!@#$%^&()].*");
    }

    public boolean checkCellphoneNumber(String num) {
        return Pattern.matches("^\\+27[0-9]{9}$", num);
    }

    public String  registerUser(String user, String pass, String num, String first, String last) {
        if (!checkUserName(user)) return "Username is not correctly formatted...";
        if (!checkPasswordComplexity(pass)) return "Password is not correctly formatted...";
        if (!checkCellphoneNumber(num)) return "Cell phone number incorrectly formatted...";

        this.savedUser = user;
        this.savedPass = pass;
        this.fName = first;
        this.lName = last;

        return "Username and Password successfully captured.";
    }

    public boolean loginUser(String user, String pass) {
        return user.equals(savedUser) && pass.equals(savedPass);
    }

    public String returnLoginStatus(boolean loggedIn) {
        return loggedIn
                ? "Welcome " + fName + ", " + lName + " it is great to see you again."
                : "Username or password incorrect, please try again.";
    }
}