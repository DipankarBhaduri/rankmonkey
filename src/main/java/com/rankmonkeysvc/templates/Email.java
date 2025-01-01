package com.rankmonkeysvc.templates;

public class Email {

    public static final String SEND_EMAIL_FOR_SET_PASSWORD =
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Email Verification</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            background-color: #f9f9f9;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .email-container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 20px auto;\n" +
            "            background: #ffffff;\n" +
            "            border-radius: 8px;\n" +
            "            overflow: hidden;\n" +
            "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
            "        }\n" +
            "        .email-header {\n" +
            "            background-color: #4CAF50;\n" +
            "            color: #ffffff;\n" +
            "            text-align: center;\n" +
            "            padding: 20px;\n" +
            "        }\n" +
            "        .email-header h1 {\n" +
            "            margin: 0;\n" +
            "            font-size: 24px;\n" +
            "        }\n" +
            "        .email-body {\n" +
            "            padding: 20px;\n" +
            "            color: #333333;\n" +
            "            line-height: 1.6;\n" +
            "        }\n" +
            "        .email-body p {\n" +
            "            margin: 10px 0;\n" +
            "        }\n" +
            "        .email-body a {\n" +
            "            display: inline-block;\n" +
            "            margin: 20px 0;\n" +
            "            padding: 10px 20px;\n" +
            "            background-color: #4CAF50;\n" +
            "            color: #ffffff;\n" +
            "            text-decoration: none;\n" +
            "            border-radius: 4px;\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .email-body a:hover {\n" +
            "            background-color: #45a049;\n" +
            "        }\n" +
            "        .email-footer {\n" +
            "            text-align: center;\n" +
            "            font-size: 12px;\n" +
            "            color: #666666;\n" +
            "            padding: 20px;\n" +
            "            background-color: #f4f4f4;\n" +
            "            border-top: 1px solid #dddddd;\n" +
            "        }\n" +
            "        .email-footer p {\n" +
            "            margin: 5px 0;\n" +
            "        }\n" +
            "        .email-footer a {\n" +
            "            color: #4CAF50;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"email-container\">\n" +
            "        <div class=\"email-header\">\n" +
            "            <h1>Welcome to RankMonkey!</h1>\n" +
            "        </div>\n" +
            "        <div class=\"email-body\">\n" +
            "            <p>Thank you for signing up to RankMonkey! We are thrilled to have you onboard.</p>\n" +
            "            <p>To get started, please verify your email address by clicking the button below:</p>\n" +
            "            <a href=\"https://example.com/verify?token=your_token_here\">Verify Your Email</a>\n" +
            "            <p>If the above link does not work, please copy and paste the following URL into your browser:</p>\n" +
            "            <p><a href=\"https://example.com/verify?token=your_token_here\">https://example.com/verify?token=your_token_here</a></p>\n" +
            "            <p>Once verified, you will be able to set your password and start exploring our platform.</p>\n" +
            "        </div>\n" +
            "        <div class=\"email-footer\">\n" +
            "            <p>If you have any questions, please feel free to <a href=\"mailto:support@rankmonkey.com\">contact our support team</a>.</p>\n" +
            "            <p>&copy; 2024 RankMonkey. All rights reserved.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
}
