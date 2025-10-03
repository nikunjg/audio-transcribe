package com.learning.audiotranscribe;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final ChatModel chatModel;

    public BooksController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(defaultValue = "Ken Kousen") String author){
        String promptMessage = """
                Generate a list of books written by the author {author}, If you are not positive that a book
                belongs to this author please don't include it.
                {format}
                """;

        var convertor = new BeanOutputConverter<>(Author.class);
        String format = convertor.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(promptMessage);
        var prompt = promptTemplate.create(Map.of("author", author, "format", format));
        var generation = chatModel.call(prompt).getResult();

        return convertor.convert(generation.getOutput().getText());
    }
}
