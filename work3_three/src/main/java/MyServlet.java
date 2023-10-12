import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.io.IOException;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取 VisitCounterListener 的实例
        ServletContext servletContext = getServletContext();
        VisitCounterListener listener = (VisitCounterListener) servletContext.getAttribute("visitCounter");

        // 获取访问计数
        int visitCount = listener.getVisitCount();

        // 使用访问计数进行操作
        // ...

        response.getWriter().println("Visit Count: " + visitCount);
    }
}

