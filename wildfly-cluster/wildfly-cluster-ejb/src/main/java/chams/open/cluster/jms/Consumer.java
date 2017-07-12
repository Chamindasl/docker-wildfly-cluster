package chams.open.cluster.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by chams on 03/07/17.
 */
@MessageDriven(name = "Consumer", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/jobQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class Consumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
            try {
                logger.info("received {}", ((TextMessage) message).getText());
            } catch (JMSException e) {
                logger.error("Error ", e);
            }
    }

}
