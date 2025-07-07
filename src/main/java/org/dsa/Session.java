package org.dsa;

import org.dsa.models.objects.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        if (currentUser != null)
            return currentUser.getId();
        return -1;
    }

    public static boolean isLoggedIn()
    {
        if(currentUser == null) return false;
        return getCurrentUser().isLoggedIn();
    }

    public static void clear() {
        currentUser = null;
    }
}
