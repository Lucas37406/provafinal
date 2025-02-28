package com.fiec.provafinal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Configuração das credenciais diretamente no código


        // Criando o cliente SQS com as credenciais e região
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1) // Região desejada
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        String nomeDaFila = "Fiec2024"; // Nome da fila do SQS
        while (true) {
            Thread.sleep(30000);  // Espera 30 segundos entre as chamadas
            try {
                // Requisição para receber a mensagem da fila SQS
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(nomeDaFila)  // URL da fila
                        .maxNumberOfMessages(1)  // Recebe uma mensagem de cada vez
                        .build();

                // Recebe as mensagens
                List<software.amazon.awssdk.services.sqs.model.Message> responses = sqsClient.receiveMessage(receiveMessageRequest).messages();
                for (software.amazon.awssdk.services.sqs.model.Message m : responses) {

                    System.out.println(m);
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(m.body());
                    String token = jsonNode.get("token").asText();  // Obtém o token da mensagem
                    sendMessage(token);  // Envia a notificação via Firebase

                    // Exclui a mensagem da fila após processá-la
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueUrl(nomeDaFila)
                            .receiptHandle(m.receiptHandle())  // Recebe o identificador da mensagem
                            .build();
                    sqsClient.deleteMessage(deleteMessageRequest);
                }

            } catch (SqsException e) {
                sqsClient.close();
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);  // Encerra o processo em caso de erro

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void sendMessage(String token) {
        System.out.println(token);
        com.fiec.provafinal.FirebaseSingleton.getInstance();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle("Notificação da Fila")
                        .setBody("Fiec2024 - Enviando notificação para você")
                        .build())
                .build();

        String resp = null;
        try {
            resp = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Successfully sent message: " + resp);
    }
}
