import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

@WebServlet("/look")
@WebListener
public class MyServletContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        // 创建 VisitCounterListener 实例并添加到 ServletContext
        VisitCounterListener listener = new VisitCounterListener();
        event.getServletContext().setAttribute("visitCounter", listener);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // 在应用程序关闭时进行清理操作
        // 可以在这里释放资源、关闭数据库连接等
    }
}
