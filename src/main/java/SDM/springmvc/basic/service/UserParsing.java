package SDM.springmvc.basic.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Slf4j
@Service
public class UserParsing {

    //바디에 "id" 랑 "pw" 키값으로 넣어서 보내기
    public JsonObject Parsing(String id, String pw) throws URISyntaxException {
        URI uri = new URI("https://auth.imsejong.com/auth?method=DosejongSession");
        RestTemplate restTemplate = new RestTemplate();

        //create Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //creat param
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("pw", pw);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(),headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, String.class);

        JsonObject response = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();
        return response;
    }

}