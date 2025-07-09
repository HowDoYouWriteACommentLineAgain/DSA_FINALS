        package org.dsa;

        import org.dsa.UIPanels.LoginPanel;
        import org.dsa.UIPanels.TabularPanels.TransactionUI;
        import org.dsa.UIPanels.components.NavigationBar;
        import org.dsa.models.objects.User;
        import org.dsa.services.TransactionService;
        import org.dsa.utils.Constants.Screens;
        import org.dsa.UIPanels.DashboardPanel;
        import org.dsa.UIPanels.components.MainFrame;
        import org.dsa.utils.DatabaseConnectionManager;

        import javax.swing.JOptionPane;
        import java.sql.Connection;

        /*TODOS:
        * Add more panels
        *
        *
        * */

        public class AppManager {

            private final Connection conn;
            private final TransactionService txSer;
            private final MainFrame mainFrame;
            private final NavigationBar navbar;

            private LoginPanel loginPanel;
            private TransactionUI transactionUI;
            private DashboardPanel dashboardPanel;

            private static final AppManager instance = new AppManager();
            public static AppManager getInstance()
            {
                return instance;
            }

            private AppManager(){
                conn = DatabaseConnectionManager.getConnection();
                txSer = new TransactionService(conn);

                mainFrame = new MainFrame("Financial Assistant");
                navbar = new NavigationBar();
            }

            public void start(){

                if(!Session.isLoggedIn()) Session.setCurrentUser(new User());//test because I have removed login in

                //go to login if session is blank

                if(Session.isLoggedIn())
                {
                    buildAfterSessionConnection();
                }

                mainFrame.showScreen(Screens.DASHBOARD);
                mainFrame.pack();
                mainFrame.setVisible(true);
            }

            private void buildAfterSessionConnection()
            {
                transactionUI = new TransactionUI(txSer);
                dashboardPanel = new DashboardPanel(txSer);

                mainFrame.addNavbar(navbar);
                mainFrame.addScreen(Screens.DASHBOARD, dashboardPanel);
                mainFrame.addScreen(Screens.TRANSACTION, transactionUI);
                refreshAll();
            }

            public void handleLogin()
            {
                mainFrame.removeNavbar();
                mainFrame.addNavbar(navbar);
                mainFrame.showScreen(Screens.DASHBOARD);
                refreshAll();
            }

            public void handleLogout()
            {
                if(loginPanel != null)
                {
                    mainFrame.removeNavbar();
                    mainFrame.showScreen(Screens.LOGIN);
                    refreshAll();
                }else{
                    shutdown();
                }
            }

            private void shutdown()
            {
                int i = JOptionPane.showConfirmDialog(mainFrame,"Confirm Exit", "Exit", JOptionPane.YES_NO_OPTION);
                if(i == 0)
                {
                    System.exit(0);
                }

            }

            public void handleNavigation(String screenName)
            {
                if(!Session.isLoggedIn())
                {
                    mainFrame.updateUI();
                    return;
                }
                refreshAll();
                mainFrame.showScreen(screenName);
            }

            private void refreshAll()
            {
                dashboardPanel.refresh();
                transactionUI.refresh();;
            }
        }
    /*
    *
            private static AppManager instance;
            public static AppManager getInstance()
            {
                if (instance == null) instance = new AppManager();
                return instance;
            }

            private final Connection conn = DatabaseConnectionManager.getConnection();
            private final TransactionService transactionService = new TransactionService(conn);
            final MainFrame mf = new MainFrame("Financial Tracker");

            public void start()
            {

                if(!Session.isLoggedIn())
                    Session.setCurrentUser(new User());

    //            var loginPanel = new LoginPanel(mf, this);
    //            mf.addScreen(Screens.LOGIN, loginPanel);
    //            mf.showScreen(Screens.LOGIN);

                mf.showNavigationBar();
                buildAfterLogin();
                mf.pack();
                mf.setVisible(true);
            }

            public void buildAfterLogin(){

                var dashboardPanel = new DashboardPanel();
                var transactionPanel = new TransactionUI(transactionService);
                //        var incomePanel = new SimpleDataPanel();
                //        var expensePanel = new SimpleDataPanel();
                //        var budgetPanel = new SimpleDataPanel();
                //        var goalsPanel = new SimpleDataPanel();




                mf.addScreen(Screens.DASHBOARD, dashboardPanel);
                mf.addScreen(Screens.TRANSACTION, transactionPanel);
                //        mf.addScreen(Screens.INCOME, incomePanel);
                //        mf.addScreen(Screens.EXPENSE,expensePanel);
                //        mf.addScreen(Screens.BUDGET, budgetPanel);
                //        mf.addScreen(Screens.GOALS, goalsPanel);

            }

            public void handleLogout()
            {

            }
    *
    * */