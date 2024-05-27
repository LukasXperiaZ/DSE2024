import pika

if __name__ == '__main__':
    # connect to rabbitmq
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost',
                                  port=5672,
                                  credentials=pika.PlainCredentials('guest', 'guest')))

    channel = connection.channel()
    channel.exchange_declare(exchange='control', exchange_type='topic', durable=True)

    # create a queue for all topics
    result = channel.queue_declare(queue='', exclusive=True)
    queue_name = result.method.queue

    # bind the queue to the exchange
    channel.queue_bind(exchange='control', queue=queue_name, routing_key='#')

    # print data from all messages in the control exchange
    def callback(ch, method, properties, body):
        # print routing key
        print(method.routing_key)

        print(body)

    channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()