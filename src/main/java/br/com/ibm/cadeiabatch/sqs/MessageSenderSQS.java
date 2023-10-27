package br.com.ibm.cadeiabatch.sqs;

import br.com.ibm.cadeiabatch.entity.Produto;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.UUID;

@Service
public class MessageSenderSQS {

    @Value("${url.sqs.queue}")
    private String queueUrl;

    final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    public void send(Produto produto) throws JsonProcessingException {

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(produto.getId().toString());
        sqs.sendMessage(send_msg_request);
    }
}
