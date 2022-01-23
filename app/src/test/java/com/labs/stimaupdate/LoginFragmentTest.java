package com.labs.stimaupdate;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class LoginFragmentTest {

    @Test
    public void chechDataEntered() {
    LoginFragment loginFragment= new LoginFragment();

    String email = "paul.okeyo@strathmore.com/";
    String password = "124sdfK124@";

    Boolean result = loginFragment.chechDataEntered(email,password);

    assertFalse(result);

    }
}