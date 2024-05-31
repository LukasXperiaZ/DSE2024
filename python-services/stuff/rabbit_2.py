import pika

if __name__ == '__main__':
    # connect to rabbitmq
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='35.234.89.219',
                                  port=5672,
                                  credentials=pika.PlainCredentials('guest', 'secret')))

    channel = connection.channel()
    channel.exchange_declare(exchange='control1', exchange_type='topic', durable=True)

    # create a queue for all topics
    result = channel.queue_declare(queue='', exclusive=True)
    queue_name = result.method.queue

    # bind the queue to the exchange
    channel.queue_bind(exchange='control1', queue=queue_name, routing_key='#')

    # print data from all messages in the control exchange
    def callback(ch, method, properties, body):
        # print routing key
        print(method.routing_key)

        print(body)

    channel.basic_publish(exchange='control1', routing_key='test', body=b'test')
    channel.start_consuming()