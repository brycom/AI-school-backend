package com.example.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

@Service
public class OpenAiService {

    @Value("${openai.api.url}")
    private String openAiApiUrl;
    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;
    private AnswerService answerService;
    private QuestionService questionService;
    private final SimpMessagingTemplate messagingTemplate;

    public OpenAiService(RestTemplate restTemplate, UserService userService, QuestionRepository questionRepository,
            AnswerService answerService, QuestionService questionService, SimpMessagingTemplate messagingTemplate) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.questionService = questionService;
        this.messagingTemplate = messagingTemplate;
    }

    /*     public ChatRespons sendQuestion(Topic topic, Teacher teacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        List<Question> lastTen = questionService.getLastTenQuestions(userId, topic.getId());
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() + "med en svårighetsgrad på:"
                        + topic.getLevel() + "av 10. Frågan ska vara kort och inget svar ska ges ",
                1,
                teacher, false);
    
        if (lastTen.size() > 0) {
            System.out.println("Storleken på context listan: " + lastTen.size());
            StringBuilder sb = new StringBuilder("här är några frågor du har ställt tidigare ställ inte dom igen:");
            for (Question question : lastTen) {
                sb.append("\n -").append(question.getQuestion());
    
            }
            chatRequest.addMessage(new Message("assistant", sb.toString()));
        }
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);
        Question question = new Question(topic.getId(), userId, respons.getChoices().get(0).getMessage().getContent(),
                false);
        questionRepository.save(question);
    
        return respons;
    } */

    /*     public ChatRespons sendQuestionStream(Topic topic, Teacher teacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        List<Question> lastTen = questionService.getLastTenQuestions(userId, topic.getId());
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() + "med en svårighetsgrad på:"
                        + topic.getLevel() + "av 10. Frågan ska vara kort och inget svar ska ges ",
                1,
                teacher, true);
    
        //chatRequest.setStream(true);
    
        if (lastTen.size() > 0) {
            StringBuilder sb = new StringBuilder("här är några frågor du har ställt tidigare ställ inte dom igen:");
            for (Question question : lastTen) {
                sb.append("\n -").append(question.getQuestion());
            }
            chatRequest.addMessage(new Message("assistant", sb.toString()));
        }
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);
        Question question = new Question(topic.getId(), userId, respons.getChoices().get(0).getMessage().getContent(),
                false);
        questionRepository.save(question);
    
        return respons;
    } */
    public void sendQuestionStream(Topic topic, Teacher teacher) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = userService.getUserIdFromUsername(username);
        List<Question> lastTen = questionService.getLastTenQuestions(userId, topic.getId());

        // Skapa ChatRequest
        ChatRequest chatRequest = new ChatRequest("gpt-4o",
                "Generera en " + topic.getTopic() + " fråga om " + topic.getDescription() +
                        " med en svårighetsgrad på: " + topic.getLevel()
                        + " av 10. Frågan ska vara kort och inget svar ska ges ",
                1,
                teacher, true);

        if (lastTen.size() > 0) {
            StringBuilder sb = new StringBuilder("Här är några frågor du har ställt tidigare, ställ inte dem igen:");
            for (Question question : lastTen) {
                sb.append("\n -").append(question.getQuestion());
            }
            chatRequest.addMessage(new Message("assistant", sb.toString()));
        }
        System.out.println("syns dethär?????");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest;
        try {
            jsonRequest = objectMapper.writeValueAsString(chatRequest);
            System.out.println("Sending request: " + jsonRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        try {
            restTemplate.execute(openAiApiUrl, HttpMethod.POST, request -> {
                request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                request.getBody().write(new ObjectMapper().writeValueAsBytes(chatRequest));
            }, response -> {
                try (InputStream inputStream = response.getBody();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String jsonData = line.substring(6);
                            if (!jsonData.equals("[DONE]")) {
                                JsonNode jsonNode = objectMapper.readTree(jsonData);
                                String content = jsonNode.at("/choices/0/delta/content").asText();
                                System.out.println(content);
                                if (!content.isEmpty()) {
                                    messagingTemplate.convertAndSend("/topic", content);
                                }
                            }
                        }
                    }

                }
                return null;
            });

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            //e.printStackTrace();
        }
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
                teacher, false);
        //chatRequest.addMessage(new Message("assistant", question.getQuestion()));
        ChatRespons respons = restTemplate.postForObject(openAiApiUrl, chatRequest, ChatRespons.class);

        try {
            AiModelAnswer aiAnswer = objectMapper.readValue(respons.getChoices().get(0).getMessage().getContent(),
                    AiModelAnswer.class);
            answerService.evaluetAnswer(answer, aiAnswer, question.getQuestion());

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return respons;
    }

}
