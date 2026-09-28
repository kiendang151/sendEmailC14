package murach.util;

import java.io.IOException;

import javax.mail.MessagingException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MailUtilLocal {

    public static void sendMail(
            String to,
            String from,
            String subject,
            String body,
            boolean bodyIsHTML)
            throws MessagingException {

        // Lấy Resend API Key từ Environment Variable
        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new MessagingException(
                    "RESEND_API_KEY chưa được cấu hình"
            );
        }

        /*
         * Resend yêu cầu địa chỉ From hợp lệ.
         * Khi đang test với tài khoản mới, dùng địa chỉ onboarding
         * của Resend.
         */
        String resendFrom = "onboarding@resend.dev";

        // Nếu body là HTML thì gửi vào trường html
        // Nếu là text thường thì chuyển thành HTML đơn giản
        String htmlBody;

        if (bodyIsHTML) {
            htmlBody = body;
        } else {
            htmlBody = body
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\n", "<br>");
        }

        // Escape JSON
        String jsonBody = "{"
                + "\"from\":\"" + escapeJson(resendFrom) + "\","
                + "\"to\":[\"" + escapeJson(to) + "\"],"
                + "\"subject\":\"" + escapeJson(subject) + "\","
                + "\"html\":\"" + escapeJson(htmlBody) + "\""
                + "}";

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType =
                MediaType.parse("application/json");

        RequestBody requestBody =
                RequestBody.create(
                        jsonBody,
                        mediaType
                );

        Request request =
                new Request.Builder()
                        .url("https://api.resend.com/emails")
                        .addHeader(
                                "Authorization",
                                "Bearer " + apiKey
                        )
                        .addHeader(
                                "Content-Type",
                                "application/json"
                        )
                        .post(requestBody)
                        .build();

        try (Response response = client.newCall(request).execute()) {

            String responseBody =
                    response.body() != null
                            ? response.body().string()
                            : "";

            System.out.println(
                    "Resend HTTP status: "
                    + response.code()
            );

            System.out.println(
                    "Resend response: "
                    + responseBody
            );

            if (!response.isSuccessful()) {
                throw new MessagingException(
                        "Resend gửi email thất bại. HTTP "
                        + response.code()
                        + ": "
                        + responseBody
                );
            }
        } catch (IOException e) {

            throw new MessagingException(
                    "Không thể kết nối Resend API: "
                    + e.getMessage(),
                    e
            );
        }
    }

    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}