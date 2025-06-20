import json
import logging
import threading

import pika

from python_services.common.config import RABBITMQ_HOST, RABBITMQ_PORT, RABBITMQ_USER, RABBITMQ_PASS
from python_services.control_service import database

logger = logging.getLogger(__name__)

# initialize rabbitmq connection for the main thread
connection = pika.BlockingConnection(
    pika.ConnectionParameters(host=RABBITMQ_HOST,
                              port=RABBITMQ_PORT,
                              credentials=pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS))
)
channel = connection.channel()
channel.exchange_declare(exchange='control', exchange_type='topic', durable=True)



class RabbitMQConsumer(threading.Thread):
    """
    RabbitMQ consumer listening to the positions in a separate thread
    """

    def __init__(self):
        threading.Thread.__init__(self)
        self.connection = None
        self.channel = None


    def run(self):
        """
        start consumer in a separate thread
        """

        # init connection for consumer thread
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=RABBITMQ_HOST,
                                      port=RABBITMQ_PORT,
                                      credentials=pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS))
        )
        self.channel = self.connection.channel()
        self.channel.exchange_declare(exchange='position', exchange_type='fanout')
        in_queue = self.channel.queue_declare(queue='', exclusive=True)
        self.channel.queue_bind(exchange='position',
                           queue=in_queue.method.queue)
        self.queue_name = in_queue.method.queue

        self.channel.basic_consume(
            queue=self.queue_name, on_message_callback=self.callback, auto_ack=True)
        self.channel.start_consuming()

    def callback(self, ch, method, properties, body):
        """
        gets updates from lvs and distributes the targets to the fvs
        """
        try:
            data = json.loads(body)
        except json.JSONDecodeError:
            logger.error(f"Could not parse message")
            return
        # check if data contains key "targetSpeed" => then it is an update from a lv
        if "targetSpeed" in data:
            # check if there is an active follow me session
            state = database.state.find_one({"lv": data["vin"]})
            if state is None:
                logger.debug(f"Vin {data['vin']} is not a leading vehicle")
            if state is not None:
                # update the target speed and lane of the follow me session
                database.state.update_one({"lv": data["vin"]}, {"$set": {"target_speed": data["targetSpeed"], "target_lane": data["targetLane"]}})
                # send the new target to the following vehicle
                self.channel.basic_publish(exchange='control',
                                      routing_key=state["fv"],
                                      body=json.dumps({
                                          "usesFM": True,
                                            "vinLV": state["lv"],
                                            "targetLane": data["targetLane"],
                                            "targetSpeed": data["targetSpeed"]
                                      }))



    def stop(self):
        self.channel.stop_consuming()
        self.connection.close()


consumer = RabbitMQConsumer()