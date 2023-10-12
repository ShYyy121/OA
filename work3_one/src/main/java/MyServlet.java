import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 创建 Cookie 对象
        Cookie cookie = new Cookie("myCookie", "cookieValue");
        // 设置最大生命时间为一小时（以秒为单位）
        cookie.setMaxAge(60 * 60);
        // 将 Cookie 添加到响应中
        response.addCookie(cookie);

        // 获取或创建 Session 对象
        HttpSession session = request.getSession();
        // 设置 Session 属性
        session.setAttribute("username", "JohnDoe");

        // 获取 Session 属性
        String username = (String) session.getAttribute("username");

        // 检查 Session 是否新创建的
        boolean isNewSession = session.isNew();

        // 获取 Session ID
        String sessionId = session.getId();

        // 销毁 Session
        session.invalidate();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
