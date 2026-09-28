package murach.email;

import java.io.IOException;

import javax.mail.MessagingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtilLocal;


@WebServlet("/emailList")
public class EmailListServlet
        extends HttpServlet {


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        // =====================================
        // DEFAULT URL
        // =====================================

        String url = "/index.jsp";


        // =====================================
        // GET ACTION
        // =====================================

        String action =
                request.getParameter("action");


        if (action == null) {

            action = "join";
        }


        // =====================================
        // JOIN
        // =====================================

        if (action.equals("join")) {

            url = "/index.jsp";
        }


        // =====================================
        // ADD
        // =====================================

        else if (action.equals("add")) {


            // ---------------------------------
            // GET DATA FROM FORM
            // ---------------------------------

            String firstName =
                    request.getParameter(
                            "firstName"
                    );


            String lastName =
                    request.getParameter(
                            "lastName"
                    );


            String email =
                    request.getParameter(
                            "email"
                    );


            // ---------------------------------
            // CREATE USER
            // ---------------------------------

            User user =
                    new User(
                            firstName,
                            lastName,
                            email
                    );


            String message = "";


            // =================================
            // CHECK EMAIL
            // =================================

            if (UserDB.emailExists(
                    user.getEmail())) {


                message =
                        "This email address already exists.<br>"
                        + "Please enter another email address.";


                url = "/index.jsp";


            } else {


                // =================================
                // SAVE USER USING JPA
                // =================================

                UserDB.insert(user);


                // =================================
                // SEND EMAIL
                // =================================

                String to =
                        email;


                String from = System.getenv("GMAIL_USERNAME");

                String subject =
                        "Welcome to our email list";


                String body =
                        "Dear "
                        + firstName
                        + ",\n\n"

                        + "Thanks for joining our email list.\n"

                        + "We'll make sure to send you "
                        + "announcements about new products "
                        + "and promotions.\n\n"

                        + "Have a great day and "
                        + "thanks again!\n\n"

                        + "Email List Team";


                boolean isBodyHTML =
                        false;


                try {


                    MailUtilLocal.sendMail(
                            to,
                            from,
                            subject,
                            body,
                            isBodyHTML
                    );


                } catch (
                        MessagingException e) {


                    String errorMessage =
                            "ERROR: Unable to send email.<br>"
                            + "Please check the SMTP server.";


                    request.setAttribute(
                            "errorMessage",
                            errorMessage
                    );


                    log(
                            "Unable to send email.",
                            e
                    );
                }


                // ---------------------------------
                // SUCCESS PAGE
                // ---------------------------------

                url = "/thanks.jsp";
            }


            request.setAttribute(
                    "user",
                    user
            );


            request.setAttribute(
                    "message",
                    message
            );
        }


        // =====================================
        // FORWARD
        // =====================================

        getServletContext()
                .getRequestDispatcher(url)
                .forward(
                        request,
                        response
                );
    }


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        request.getRequestDispatcher(
                "/index.jsp"
        ).forward(
                request,
                response
        );
    }
}