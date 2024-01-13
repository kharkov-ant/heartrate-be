#include <ArduinoJson.h>
#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress ip(192, 168, 50, 2);
IPAddress server(192, 168, 50, 128);
unsigned long currentMillis;
const int buttonPin = 2;
int buttonState = 0;
const int bufferSize = 100;
int heartAmplitude[bufferSize];
int time[bufferSize];
unsigned long amplitude;
int i = 0;
EthernetClient ethClient;
PubSubClient mqttClient(ethClient);

void callback(char *topic, byte *payload, unsigned int length)
{
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println();
}

void reconnect()
{
  // Loop until we're reconnected
  while (!mqttClient.connected())
  {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (mqttClient.connect("arduinoClient"))
    {
      Serial.println("connected");
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setup()
{
  Serial.begin(9600);                 // initialize the serial communication:
  pinMode(5, INPUT);                 // Setup for leads off detection LO +
  pinMode(6, INPUT);                 // Setup for leads off detection LO -
  pinMode(buttonPin, INPUT);          // initialize the pushbutton pin as an input:
  mqttClient.setServer(server, 1883); // mqttClient.setCallback(callback);
  Ethernet.begin(mac, ip);            // Allow the hardware to sort itself out
  delay(1500);
}

void loop()
{
  if (!mqttClient.connected())
  {
    reconnect();
  }
  mqttClient.loop();
  buttonState = digitalRead(buttonPin);
  if (buttonState == HIGH)
  {
    // Serial.println("Enabled");
    while (buttonState == HIGH)
    {
      if ((digitalRead(5) == 1)||(digitalRead(6) == 1))
      {
        Serial.println("No signal");
      }
      else
      {
        amplitude = analogRead(A0); // send the value of analog input 0:
        currentMillis = millis();
        Serial.print(currentMillis);Serial.print(",");Serial.println(amplitude);
        heartAmplitude[i] = amplitude;
        time[i] = currentMillis;
        i++;
        if (i >= bufferSize) //better to send buffered data async
        {
          for (int j = 0; j < bufferSize; j++)
          {
            DynamicJsonDocument jsonDoc(100);
            jsonDoc["time"] = time[j];
            jsonDoc["amplitude"] = heartAmplitude[j];
            String jsonString;
            serializeJson(jsonDoc, jsonString);
            Serial.println(jsonString);
            mqttClient.publish("outTopic", jsonString.c_str());
          }
          i = 0;
        }
        // Wait for a bit to keep serial data from saturating
        delay(1);
      }
      buttonState = digitalRead(buttonPin);
    }
  }
}
