package bd.edu.seu.onlinequiz.model;

public class UserSession {
    private static User currentUser;
    private static String loggedInUsername;
    private static String loggedInRole;

    public static void setLoggedInUser(String username, String role) {
        loggedInUsername = username;
        loggedInRole = role;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static String getLoggedInRole() {
        return loggedInRole;
    }


    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }

    public static boolean validateSession() {
        return currentUser != null &&
                currentUser.getUsername() != null &&
                !currentUser.getUsername().trim().isEmpty();
    }

}
