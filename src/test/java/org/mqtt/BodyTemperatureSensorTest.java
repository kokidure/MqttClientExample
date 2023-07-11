package org.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BodyTemperatureSensorTest
{
    private static Logger log = LoggerFactory.getLogger(BodyTemperatureSensorTest.class);

    @Test
    public void sendSingleMessage() throws Exception
    {
        String publisherId = UUID.randomUUID().toString();
        MqttClient publisher = new MqttClient("tcp://test.mosquitto.org:1883",publisherId);

        String subscriberId = UUID.randomUUID().toString();
        MqttClient subscriber = new MqttClient("tcp://test.mosquitto.org:1883",subscriberId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);


        subscriber.connect(options);
        publisher.connect(options);

        CountDownLatch receivedSignal = new CountDownLatch(1);

        subscriber.subscribe(BodyTemperatureSensor.TOPIC, (topic, msg) -> {
            byte[] payload = msg.getPayload();
            log.info("Message received: topic={}, payload={}", topic, new String(payload));
            receivedSignal.countDown();
        });

        Callable<Void> target = new BodyTemperatureSensor(publisher);
        target.call();

        receivedSignal.await(1, TimeUnit.MINUTES);

        log.info("Success !");
    }

    @Test
    public void sendMultipleMessages() throws Exception {
        String publisherId = UUID.randomUUID().toString();
        MqttClient publisher = new MqttClient("tcp://test.mosquitto.org:1883",publisherId);

        String subscriberId = UUID.randomUUID().toString();
        MqttClient subscriber = new MqttClient("tcp://test.mosquitto.org:1883",subscriberId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
    }
}
