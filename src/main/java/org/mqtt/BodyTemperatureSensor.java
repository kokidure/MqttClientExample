package org.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Body temperature sensor for MQTT
 *
 */
public class BodyTemperatureSensor implements Callable<Void>
{
    private static final Logger logger = LoggerFactory.getLogger(BodyTemperatureSensor.class);
    public static final String TOPIC = "body/temperature";

    private final IMqttClient client;
    private final Random random = new Random();

    public BodyTemperatureSensor(IMqttClient client) {
        this.client = client;
    }

    @Override
    public Void call() throws Exception {

        if ( !client.isConnected() ) {
            logger.error("MQTT client is not connected");
            return null;
        }

        MqttMessage msg = readBodyTemperature();

        client.publish(TOPIC, msg);

        return null;
    }

    private MqttMessage readBodyTemperature() {
        double temperature = 36.5 + random.nextDouble();
        MqttMessage msg = new MqttMessage(String.valueOf(temperature).getBytes());
        msg.setQos(0);
        msg.setRetained(true);
        return msg;
    }
}
