package kr.mafoo.user.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import kr.mafoo.user.service.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageService {

    private final FirebaseMessaging firebaseMessaging;

    public Mono<Void> sendMessage(MessageDto messageDto) {
        return (messageDto.tokens().size() == 1)
            ? sendMessageToSingleMember(messageDto)
            : sendMessageToMultipleMember(messageDto);
    }

    public Mono<Void> sendMessageToSingleMember(MessageDto messageDto) {
        Message message = Message.builder()
            .setToken(messageDto.tokens().get(0))
            .setNotification(
                Notification.builder()
                    .setTitle(messageDto.title())
                    .setBody(messageDto.body())
                    .build()
            )
            .putData("route", messageDto.route())
            .putData("key", messageDto.key())
            .putData("buttonType", messageDto.buttonType().toString())
            .build();

        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendAsync(message)))
            .then();
    }

    public Mono<Void> sendMessageToMultipleMember(MessageDto messageDto) {
        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(messageDto.tokens())
            .setNotification(
                Notification.builder()
                    .setTitle(messageDto.title())
                    .setBody(messageDto.body())
                    .build()
            )
            .putData("route", messageDto.route())
            .putData("key", messageDto.key())
            .putData("buttonType", messageDto.buttonType().toString())
            .build();

        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendEachForMulticastAsync(message)))
            .then();
    }

    public Mono<Void> sendDynamicMessageToMultipleMember(List<MessageDto> messageDtoList) {

        List<MulticastMessage> multicastMessages = new java.util.ArrayList<>();

        for (int i = 0; i < messageDtoList.size(); i += 500) {
            List<MessageDto> batchMessages = messageDtoList.subList(i, Math.min(i + 500, messageDtoList.size()));

            MulticastMessage.Builder builder = MulticastMessage.builder();
            for (MessageDto dto : batchMessages) {
                builder.addAllTokens(dto.tokens())
                    .setNotification(
                        Notification.builder()
                            .setTitle(dto.title())
                            .setBody(dto.body())
                            .build()
                    )
                    .putData("route", dto.route())
                    .putData("key", dto.key())
                    .putData("buttonType", dto.buttonType().toString());
            }
            multicastMessages.add(builder.build());
        }

        List<Mono<Void>> sendOperations = multicastMessages.stream()
            .map(message -> Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendEachForMulticastAsync(message))).then())
            .toList();

        return Mono.when(sendOperations);
    }

    private <T> CompletableFuture<T> toCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        apiFuture.addListener(() -> {
            try {
                completableFuture.complete(apiFuture.get());
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }, Runnable::run);
        return completableFuture;
    }

}
