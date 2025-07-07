package org.dsa.abstractions;

import org.dsa.UIPanels.components.NavigationBar;

public interface FrameNavigator {
    void showScreen(String name);
    void showNavigationBar();
    void setNavbar(NavigationBar navbar);
}
