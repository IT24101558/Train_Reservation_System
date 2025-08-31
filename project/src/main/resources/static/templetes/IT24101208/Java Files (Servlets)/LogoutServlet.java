@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("HTML/login.jsp");
    }
}
