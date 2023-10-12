import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class VisitCounterListener implements ServletContextListener {
    private int visitCount = 0;

    public void contextInitialized(ServletContextEvent event) {
        // 在应用程序启动时初始化访问计数为0
        event.getServletContext().setAttribute("visitCount", visitCount);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // 在应用程序关闭时进行清理操作
        // 可以在这里保存访问计数到数据库或其他持久化方式
    }

    public void incrementVisitCount() {
        visitCount++;
    }

    public int getVisitCount() {
        return visitCount;
    }
}
