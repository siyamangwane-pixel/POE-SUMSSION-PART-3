/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poe2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testCheckUserName() {
        Login login = new Login();

        assertTrue(login.checkUserName("a_b"));     
        assertFalse(login.checkUserName("abc"));    
        assertFalse(login.checkUserName("abcdef")); 
    }

    @Test
    public void testCheckPasswordComplexity() {
        Login login = new Login();

        assertTrue(login.checkPasswordComplexity("Abcdef1!"));
        assertFalse(login.checkPasswordComplexity("abcdefg")); 
    }

    @Test
    public void testCheckCellphoneNumber() {
        Login login = new Login();

        assertTrue(login.checkCellphoneNumber("+27831234567"));
        assertFalse(login.checkCellphoneNumber("0831234567"));
    }

    @Test
    public void testRegisterUser() {
        Login login = new Login();

        String result = login.registerUser("a_b", "Abcdef1!", "+27831234567", "Siya", "Mangwane");
        assertEquals("Username and Password successfully captured.", result);
    }

    @Test
    public void testLoginUser() {
        Login login = new Login();

        login.registerUser("a_b", "Abcdef1!", "+27831234567", "Siya", "Mangwane");

        assertTrue(login.loginUser("a_b", "Abcdef1!"));
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    @Test
    public void testReturnLoginStatus() {
        Login login = new Login();

        login.registerUser("a_b", "Abcdef1!", "+27831234567", "Siya", "Mangwane");

        String successMsg = login.returnLoginStatus(true);
        String failMsg = login.returnLoginStatus(false);

        assertTrue(successMsg.contains("Welcome"));
        assertEquals("Username or password incorrect, please try again.", failMsg);
    }
}