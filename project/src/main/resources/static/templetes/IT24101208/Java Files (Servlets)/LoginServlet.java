@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        if ("admin@example.com".equals(email) && "admin".equals(password)) {
            User user = new User("Admin", email, password);
            request.getSession().setAttribute("user", user);
            response.sendRedirect("HTML/profile.jsp");
        } else {
            response.sendRedirect("HTML/login.jsp");
        }
    }
}
