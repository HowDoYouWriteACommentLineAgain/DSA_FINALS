        package org.dsa;

        import org.dsa.UIPanels.TabularPanels.IncomeTablePanel;
        import org.dsa.UIPanels.components.NavigationBar;
        import org.dsa.abstractions.GenericService;
        import org.dsa.dao.IncomeDAO;
        import org.dsa.models.objects.Income;
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

            private final MainFrame mainFrame;
            private final NavigationBar navbar;

            private DashboardPanel dashboardPanel;

            private final GenericService<Income, IncomeDAO> inSer;
            private IncomeTablePanel incomeUIPanel;

//            private final GenericService

            private static final AppManager instance = new AppManager();
            public static AppManager getInstance()
            {
                return instance;
            }

            private AppManager(){
                conn = DatabaseConnectionManager.getConnection();
                inSer = new GenericService<>(new IncomeDAO(conn));

//                loginPanel = new LoginPanel();
                mainFrame = new MainFrame("Financial Assistant");
                navbar = new NavigationBar();
            }

            public void start(){

                build();

                mainFrame.showScreen(Screens.DASHBOARD);
                mainFrame.pack();
                mainFrame.setVisible(true);

                System.out.print(inSer.getAll());
                incomeUIPanel.refresh();
            }

            private void build()
            {
                incomeUIPanel = new IncomeTablePanel(inSer);
                dashboardPanel = new DashboardPanel(inSer);

                mainFrame.addNavbar(navbar);
                mainFrame.addScreen(Screens.DASHBOARD, dashboardPanel);
                mainFrame.addScreen(Screens.INCOME, incomeUIPanel);
                refreshAll();
            }

            public void handleLogout()
            {
                shutdown();
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
                refreshAll();
                mainFrame.showScreen(screenName);
            }

            private void refreshAll()
            {
                dashboardPanel.refresh();
                incomeUIPanel.refresh();
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