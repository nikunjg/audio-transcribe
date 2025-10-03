package com.learning.audiotranscribe;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/olympics")
public class OlympicsController {

    private final ChatModel chatModel;

    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olympicSportsResource;

    @Value("classpath:/docs/olympics-sports.txt")
    private Resource contextResource;


    public OlympicsController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/2024")
    public String olympic2024Data(
            @RequestParam(defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
            @RequestParam(defaultValue = "false") boolean stuffIt) {
        PromptTemplate promptTemplate = new PromptTemplate(olympicSportsResource);
        Map<String, Object> map = new HashMap<>();
        map.put("question", message);

        if (stuffIt) {
            map.put("context", contextResource);
        } else {
            map.put("context", "");
        }

        Prompt prompt = promptTemplate.create(map);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
