package za.co.imqs.RabbitMQMessagingDemo;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class RabbitMqMessagingDemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(RabbitMqMessagingDemoApplication.class, args);

	}

//	@Bean
//	public ApplicationRunner runner(AmqpTemplate template) {
//		return args -> template.convertAndSend("myExchange", "foo.bar.baz","Hello World!");
//	}

	@Bean
	public Queue queue1() {
		return new Queue("queue1");
	}

	@Bean
	public Queue queue2() {
		return new Queue("queue2");
	}

	@Bean
	public TopicExchange myExchange() {
		return new TopicExchange("myExchange");
	}

	@Bean
	Binding binding1(Queue queue1, TopicExchange myExchange, @Value("{routing.key}") String routingKey) {
		return BindingBuilder.bind(queue1).to(myExchange).with(routingKey);
	}

	@Bean
	Binding binding2(Queue queue2, TopicExchange myExchange) {
		return BindingBuilder.bind(queue2).to(myExchange).with("foo.bar.#");
	}

	@RabbitListener(queues = "${queue.queue1.name}")
	public void listen(Greeting in) {
		System.out.println(in.getMessage());
	}

}
