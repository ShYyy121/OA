// MyServlet.java
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取 ServletContext 对象
        ServletContext context = getServletContext();

        // 获取上下文参数的值
        String paramValue = context.getInitParameter("myParam");

        // 使用上下文参数的值进行操作
        // ...

        response.getWriter().println("Context Parameter Value: " + paramValue);
    }
}
