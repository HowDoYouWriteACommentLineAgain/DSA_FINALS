package org.dsa.interfaces;

import org.dsa.UIPanels.components.NavigationBar;

public interface FrameController {
    void authLogin();
    void authLogout();
    void showScreen(String name);
    void showNavigationBar();
    void setNavbar(NavigationBar navbar);
}
