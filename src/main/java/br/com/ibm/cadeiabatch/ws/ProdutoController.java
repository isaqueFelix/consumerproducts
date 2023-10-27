package br.com.ibm.cadeiabatch.ws;

import br.com.ibm.cadeiabatch.entity.Produto;
import br.com.ibm.cadeiabatch.repository.ProdutoRepository;
import br.com.ibm.cadeiabatch.sqs.MessageSenderSQS;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProdutoController {

    @Value("${url.sqs.queue}")
    private String queueUrl;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProdutoController.class);

    final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Autowired
    private AmazonSNS snsClient;

    @Autowired
    private Topic productEventsTopic;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MessageSenderSQS messageSenderSQS;

    @GetMapping("/")
    public String listarHomeProdutos(Model model, @RequestParam(required = false) String description) {
        if (description != null) {
            List<Produto> produtos = produtoRepository.findByDescricaoContains(description);
            if(!produtos.isEmpty()){
                produtos.stream().forEach(
                        produto -> {
                            produto.setBase64Img(new String(produto.getImagem()));
                        }
                );

                model.addAttribute("produtos", produtos);
            }
        } else {
            List<Produto> produtos = produtoRepository.findAll();
            produtos.stream().forEach(
                    produto -> produto.setBase64Img(new String(produto.getImagem()))
            );
            model.addAttribute("produtos", produtos);
        }
        return "listaProdutos";
    }

    @GetMapping("/catalog")
    public String listarProdutos(Model model, @RequestParam(required = false) String description) {
        if (description != null) {
            List<Produto> produtos = produtoRepository.findByDescricaoContains(description);
            if(!produtos.isEmpty()){
                produtos.stream().forEach(
                        produto -> {
                            produto.setBase64Img(new String(produto.getImagem()));
                        }
                );

                model.addAttribute("produtos", produtos);
            }
        } else {
            List<Produto> produtos = produtoRepository.findAll();
            produtos.stream().forEach(
                    produto -> produto.setBase64Img(new String(produto.getImagem()))
            );
            model.addAttribute("produtos", produtos);
        }
        return "listaProdutos";
    }

    @GetMapping("/order/create")
    public String listarOrders(Model model) {
        return "listaOrders";
    }

    @PostMapping("/order/create")
    public String inserirProduto(@RequestParam("descricao") String descricao,
                                 @RequestParam("price") String price,
                                 @RequestParam("imagem") MultipartFile imagem) throws IOException {
        Produto produto = new Produto();
        produto.setDescricao(descricao);
        produto.setPrice(Double.parseDouble(price));
        byte[] image = Base64.encodeBase64(imagem.getBytes());
        produto.setImagem(image);
        produto.setStatus(1);

        UUID id = produtoRepository.save(produto).getId();
        messageSenderSQS.send(produto);

        PublishResult publishResult = snsClient.publish(
            productEventsTopic.getTopicArn(),
            "New Product Request: " + produto.getDescricao() );

        return "redirect:/catalog";
    }

    @GetMapping("/catalog/order")
    public String listarAprovacaoProdutos(Model model, @RequestParam(required = false) UUID id) {

        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        messages.stream().forEach(
                message -> {
                    String message_id = message.getBody();
                    Optional<Produto> produto = produtoRepository.findById(UUID.fromString(message_id));
                    if(produto.isPresent()) {
                        if (produto.get().getStatus() == 1){
                            produto.get().setStatus(2);
                            produtoRepository.save(produto.get());
                        }
                    }
                }
        );

        if (id != null) {
            Optional<Produto> produto = produtoRepository.findById(id);
            if(produto.isPresent()){
                Produto produto_aux = produto.get();
                produto_aux.setBase64Img(new String(produto_aux.getImagem()));
                model.addAttribute("produtos", Arrays.asList(produto_aux));
            }
        } else {
            List<Produto> produtos = produtoRepository.findAll();
            produtos.stream().forEach(
                    produto -> produto.setBase64Img(new String(produto.getImagem()))
            );
            model.addAttribute("produtos", produtos);
        }
        return "listaAprovacaoProdutos";
    }

    @PostMapping("/catalog/order/{id}")
    public String aprovarProduto(@PathVariable(value = "id") String id) throws IOException {
        Optional<Produto> produto = produtoRepository.findById(UUID.fromString(id));
        if(produto.isPresent()) {
            produto.get().setStatus(3);
            produtoRepository.save(produto.get());
        }
        return "redirect:/catalog/order";
    }
}


