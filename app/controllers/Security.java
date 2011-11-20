package controllers;

import models.User;

public class Security extends Secure.Security
{

    static boolean authenticate(final String username, final String password)
    {
        return true;
    }

    static void onDisconnected()
    {
        Application.index();
    }

    static void onAuthenticated()
    {
        Admin.index();
    }

    static boolean check(final String profile)
    {
        if ("admin".equals(profile))
        {
            return User.find("byEmail", connected()).<User> first().isAdmin;
        }
        return false;
    }

}
