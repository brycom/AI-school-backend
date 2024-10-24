package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Models.Answer;
import com.example.Models.Question;
import com.example.Models.Teacher;
import com.example.Models.Topic;
import com.example.Models.Dtos.AiModelAnswer;
import com.example.Models.OpenAiModels.ChatRequest;
import com.example.Models.OpenAiModels.ChatRespons;
import com.example.Models.OpenAiModels.Message;
import com.example.Repositorys.QuestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
public class OpenAiService {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;
    private AnswerService answerService;

    public OpenAiService(RestTemplate restTemplate, UserService userService, QuestionRepository questionRepository,
            AnswerService answerService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.questionRepository = questionRepository;
        this.answerService = answerService;
    }

    public ChatRespons sendQuestion(Topic topic, Teacher teacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() + "med en svårighetsgrad på:"
                        + topic.getLevel() + "av 10. Frågan ska vara kort och inget svar ska ges ",
                1,
                teacher);
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);
        Question question = new Question(topic.getId(), userId, respons.getChoices().get(0).getMessage().getContent(),
                false);
        questionRepository.save(question);

        return respons;
    }

    public ChatRespons sendAnswer(Answer answer, Question question, Teacher teacher) {
        System.out.println("Mitt svar: " + answer.getAnswer());
        ChatRequest chatRequest = new ChatRequest("gpt-4",
                "Bedöm följande svar:\n" +
                        "Fråga: " + question.getQuestion() + "\n" +
                        "Svar: " + answer.getAnswer() + "\n\n" +
                        "Returnera bedömningen som JSON med följande fält:\n" +
                        "- \"correct\": true eller false (om svaret är rätt eller fel)\n" +
                        "- \"feedback\":om svaret är rätt börja med Bra sen en kort beskrivning om hur man skulle komma fram till rätt svar (max 30 ord). om det är fel börja med tyvärr tänk på detta när du löser uppgiften, och ge ett kort tips men inte svaret.(max 20 ord)\n"
                        +
                        "Var noggrann med att acceptera olika korrekta format (t.ex. 0,5 och 1/2).",
                1,
                teacher);
        //chatRequest.addMessage(new Message("assistant", question.getQuestion()));
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        try {
            AiModelAnswer aiAnswer = objectMapper.readValue(respons.getChoices().get(0).getMessage().getContent(),
                    AiModelAnswer.class);
            answerService.evaluetAnswer(aiAnswer, question.getQuestion());
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return respons;
    }

}
