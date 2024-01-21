package Services;

import com.sendgrid.*;
import model.ConfirmationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    public String sendVerificationLink(ConfirmationToken confirmationToken, String recipientEmail) throws IOException {
        Email from = new Email("nomadteam27@gmail.com");
        String subject = "Nomad app verification link";
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain", "To confirm your account, please click here :" +
                "http://localhost:8080/auth/confirm-account?token="+confirmationToken.getConfirmationToken());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }
    }
}