import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import org.owasp.encoder.Encode;

@WebServlet("/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = request.getParameter("userId");
        String processedUserId = processUserId(userId);
        addUser(processedUserId);

        // Sanitize before output to prevent XSS
        response.getWriter().println("User ID: " + Encode.forHtml(processedUserId));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userComment = request.getParameter("comment");
        String storedComment = storeComment(userComment);
        printComment(response, storedComment);
    }

    protected void searchUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchQuery = request.getParameter("search");
        String validatedQuery = validateSearchQuery(searchQuery);
        String result = executeSearch(validatedQuery);

        // Sanitize output
        response.getWriter().println("Search result for: " + Encode.forHtml(result));
    }

    private String processUserId(String userId) {
        if (userId == null || userId.isEmpty() || !userId.matches("[a-zA-Z0-9_]+")) {
            return "Invalid user ID";
        }
        return Encode.forHtml(userId);
    }

    private void addUser(String userId) {
        System.out.println("Adding user with ID: " + userId);
    }

    private String storeComment(String comment) {
        // Sanitize before storing/returning
        return Encode.forHtml(comment);
    }

    private void printComment(HttpServletResponse response, String comment) throws IOException {
        // Comment already sanitized in storeComment()
        response.getWriter().println("User Comment: " + comment);
    }

    private String validateSearchQuery(String query) {
        if (query != null && query.matches("[a-zA-Z0-9 ]+")) {
            return Encode.forHtml(query);
        } else {
            return "Invalid search query";
        }
    }

    private String executeSearch(String query) {
        // Sanitize before returning to avoid unsafe HTML rendering
        return Encode.forHtml("Search results for: " + query);
    }
}
