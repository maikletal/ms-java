package com.prosegur.integration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQClient {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String user;
    @Value("${spring.rabbitmq.password}")
    private String pass;
    @Value("${spring.rabbitmq.virtual-host}")
    private String vhost;

    private Connection connection;

    public RabbitMQClient() {}

    public RabbitMQClient(String host, Integer port, String user, String pass, String vhost) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.vhost = vhost;
        this.connect();
    }

    public void connect() {
        try {
            if (connection == null) {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(host);
                factory.setUsername(user);
                factory.setPassword(pass);
                factory.setVirtualHost(vhost);
                factory.setPort(port);
                connection = factory.newConnection();
                logger.info("Connection RabbitMQ Successfull");
            } else {
                logger.info("Connection RabbitMQ is opening");
            }
        } catch (TimeoutException | IOException e) {
            logger.error("Connection Error: " + e.getMessage(), e);
        }
    }

    public String send(String queue_name, String message) {
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            channel.basicPublish("", queue_name, null, message.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + message + "'");
            channel.close();
            return "ok";
        } catch (Exception e) {
            logger.error(" [-] Failed Sent '" + message + "'");
            logger.error("Error: " + e.getMessage(), e);
            return e.getMessage();
        }
    }

    public void recv(String queue_name) {
        try {
            Channel channel = connection.createChannel();

            channel.queueDeclare(queue_name, false, false, false, null);
            logger.info(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queue_name, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
