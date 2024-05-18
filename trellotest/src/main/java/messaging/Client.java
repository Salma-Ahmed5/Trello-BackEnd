package messaging;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import Model.Card;

@Startup
@Singleton
public class Client {
    
    @Resource(mappedName = "java:/jms/queue/DLQ")
    private Queue queue;
    
    @Inject
    private JMSContext jmsContext;
    
    public void sendMessage(String message) {
        JMSProducer producer = jmsContext.createProducer();
        producer.send((Destination) queue, message);
        System.out.println("Message sent: " + message);
    }
    
    
    public String receiveMessage() {
        JMSConsumer consumer = jmsContext.createConsumer(queue);
        String message = consumer.receiveBody(String.class);
        System.out.println("Message received: " + message);
        return message;
    } 

}
