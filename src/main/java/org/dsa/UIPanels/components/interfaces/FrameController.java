package org.dsa.UIPanels.components.interfaces;

import org.dsa.UIPanels.components.NavigationBar;

public interface FrameController {
    void authLogin();
    void authLogout();
    void showScreen(String name);
    void showNavigationBar();
}
