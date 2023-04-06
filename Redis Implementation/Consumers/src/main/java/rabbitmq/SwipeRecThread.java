package rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swipe recording thread extends from Consumer Thread to
 * record the swiper and swipee into the hashmap
 */
public class SwipeRecThread extends ConsumerThread{
  private BlockingQueue<String[]> buffer;
  /**
   * Create new swipe recording thread based on given arguments
   * @param connection  RabbitMQ connection
   * @param exchangeName  Target ExchangeName
   * @param exchangeType  Target Exchange type, either "direct", "topic" or "fanout"
   * @param queueName   Name of target Queue
   * @param bindingKeys   binding keys
   * @param buffer  an instance of Redis Command async() api
   * @throws IOException
   */
  public SwipeRecThread(
      Connection connection,
      String exchangeName,
      String exchangeType,
      String queueName,
      String[] bindingKeys,
      BlockingQueue<String[]> buffer,
      int preFetchCount) throws IOException {
    super(connection, exchangeName, exchangeType, queueName, bindingKeys, preFetchCount);
    this.buffer = buffer;
  }

  /**
   * Custom implementation of run method
   */
  @Override
  public void run() {

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String[] messages = new String(delivery.getBody(), "UTF-8").split(":");
      messages[0] = PREFIX_SWIPE_REC +messages[0];
      try {
        //use put to ensure always try to put the messages
        this.buffer.put(messages);
      } catch (InterruptedException e) {
        System.out.println("Rabbit interrupt exception");
      }
    };
    try {
      this.channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> { });
    } catch (IOException e) {
      Logger.getLogger(SwipeRecThread.class.getName()).log(Level.WARNING, "channel subscription fail", e);
      System.out.println("channel subscription fail");
    }
  }
}
