package kr.mafoo.user.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
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
            .setNotification(Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build())
            .build();

        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendAsync(message)))
            .then();
    }

    public Mono<Void> sendMessageToMultipleMember(MessageDto messageDto) {
        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(messageDto.tokens())
            .setNotification(Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build())
            .build();

        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendEachForMulticastAsync(message)))
            .then();
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
