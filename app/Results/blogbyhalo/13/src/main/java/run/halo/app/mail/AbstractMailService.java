package run.halo.app.mail;
 import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;
import run.halo.app.exception.EmailException;
import run.halo.app.model.properties.EmailProperties;
import run.halo.app.service.OptionService;
import run.halo.app.Interface.OptionService;
@Slf4j
public class AbstractMailService implements MailService{

 private  int DEFAULT_POOL_SIZE;

 protected  OptionService optionService;

 private  JavaMailSender cachedMailSender;

 private  MailProperties cachedMailProperties;

 private  String cachedFromName;

@Nullable
 private  ExecutorService executorService;

protected AbstractMailService(OptionService optionService) {
    this.optionService = optionService;
}
public InternetAddress getFromAddress(JavaMailSender javaMailSender){
    Assert.notNull(javaMailSender, "Java mail sender must not be null");
    if (StringUtils.isBlank(this.cachedFromName)) {
        // set personal name
        this.cachedFromName = optionService.getByPropertyOfNonNull(EmailProperties.FROM_NAME).toString();
    }
    if (javaMailSender instanceof JavaMailSenderImpl) {
        // get user name(email)
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
        String username = mailSender.getUsername();
        // build internet address
        return new InternetAddress(username, this.cachedFromName, mailSender.getDefaultEncoding());
    }
    throw new UnsupportedOperationException("Unsupported java mail sender: " + javaMailSender.getClass().getName());
}


@NonNull
public MailProperties getMailProperties(){
    if (cachedMailProperties == null) {
        // create mail properties
        MailProperties mailProperties = new MailProperties(log.isDebugEnabled());
        // set properties
        mailProperties.setHost(optionService.getByPropertyOrDefault(EmailProperties.HOST, String.class));
        mailProperties.setPort(optionService.getByPropertyOrDefault(EmailProperties.SSL_PORT, Integer.class));
        mailProperties.setUsername(optionService.getByPropertyOrDefault(EmailProperties.USERNAME, String.class));
        mailProperties.setPassword(optionService.getByPropertyOrDefault(EmailProperties.PASSWORD, String.class));
        mailProperties.setProtocol(optionService.getByPropertyOrDefault(EmailProperties.PROTOCOL, String.class));
        this.cachedMailProperties = mailProperties;
    }
    return this.cachedMailProperties;
}


@NonNull
public ExecutorService getExecutorService(){
    if (this.executorService == null) {
        this.executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
    }
    return executorService;
}


@Override
public void testConnection(){
    JavaMailSender javaMailSender = getMailSender();
    if (javaMailSender instanceof JavaMailSenderImpl) {
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new EmailException("无法连接到邮箱服务器，请检查邮箱配置.[" + e.getMessage() + "]", e);
        }
    }
}


public void sendMailTemplate(boolean tryToAsync,Consumer<MimeMessageHelper> callback){
    ExecutorService executorService = getExecutorService();
    if (tryToAsync) {
        // send mail asynchronously
        executorService.execute(() -> sendMailTemplate(callback));
    } else {
        // send mail synchronously
        sendMailTemplate(callback);
    }
}


@NonNull
public JavaMailSender getMailSender(){
    if (this.cachedMailSender == null) {
        // create mail sender factory
        MailSenderFactory mailSenderFactory = new MailSenderFactory();
        // get mail sender
        this.cachedMailSender = mailSenderFactory.getMailSender(getMailProperties());
    }
    return this.cachedMailSender;
}


public void clearCache(){
    this.cachedMailSender = null;
    this.cachedFromName = null;
    this.cachedMailProperties = null;
    log.debug("Cleared all mail caches");
}


public void printMailConfig(){
    if (!log.isDebugEnabled()) {
        return;
    }
    // get mail properties
    MailProperties mailProperties = getMailProperties();
    log.debug(mailProperties.toString());
}


public void setExecutorService(ExecutorService executorService){
    this.executorService = executorService;
}


}