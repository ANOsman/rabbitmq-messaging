package za.co.imqs.RabbitMQMessagingDemo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Value("${queue.queue2.name}")
    private String queue2;
    @Value("${exchange.name}")
    private String exchange;
    @Value("${routing.key}")
    private String routingKey;
    private RabbitTemplate template;

    public HelloController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping
    public Greeting createMessage(@RequestBody Greeting message) {
        template.convertAndSend(exchange, "foo.bar.pay", message,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties props = message.getMessageProperties();
                        props.setHeader("content_type", "application/json");
                        return message;
                    }
                });
        return message;
    }

    @GetMapping
    public Greeting hello() {
        return (Greeting) template.receiveAndConvert(queue2);
    }
}
