package app.controller;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static app.service.BlockManager.MineBlock;
import static app.utils.RequestUtil.GetRequest;

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT})
@EnableScheduling
@RestController
public class BlockchainController {

    @Scheduled(fixedRate = 5000)
    public void createBlock() {
        MineBlock();
    }

    @GetMapping(value = "/")
    public String home() {
        final String uri = "http://localhost:8080/";
        String result = GetRequest(uri, String.class);
        System.out.println(result);
        return "Server is running";
    }


}
