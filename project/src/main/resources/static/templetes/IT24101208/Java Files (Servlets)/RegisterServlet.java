@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        User user = new User(name, email, password);
        request.getSession().setAttribute("user", user);

        response.sendRedirect("HTML/profile.jsp");
    }
}