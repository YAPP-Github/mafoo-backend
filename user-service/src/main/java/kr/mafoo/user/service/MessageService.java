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
        Message message = buildMessage(messageDto);
        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendAsync(message))).then();
    }

    public Mono<Void> sendMessageToMultipleMember(MessageDto messageDto) {
        MulticastMessage message = buildMulticastMessage(messageDto);
        return Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendEachForMulticastAsync(message))).then();
    }

    public Mono<Void> sendDynamicMessageToMultipleMember(List<MessageDto> messageDtoList) {
        List<MulticastMessage> multicastMessages = new java.util.ArrayList<>();

        for (int i = 0; i < messageDtoList.size(); i += 500) {
            List<MessageDto> batchMessages = messageDtoList.subList(i, Math.min(i + 500, messageDtoList.size()));
            multicastMessages.addAll(batchMessages.stream().map(this::buildMulticastMessage).toList());
        }

        List<Mono<Void>> sendOperations = multicastMessages.stream()
            .map(message -> Mono.fromFuture(toCompletableFuture(firebaseMessaging.sendEachForMulticastAsync(message))).then())
            .toList();

        return Mono.when(sendOperations);
    }

    private Message buildMessage(MessageDto messageDto) {
        Message.Builder builder = Message.builder()
            .setToken(messageDto.tokens().get(0))
            .setNotification(Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build()
            )
            .putData("notificationId", messageDto.notificationId())
            .putData("route", messageDto.route());

        addOptionalData(builder, messageDto);
        return builder.build();
    }

    private MulticastMessage buildMulticastMessage(MessageDto messageDto) {
        MulticastMessage.Builder builder = MulticastMessage.builder()
            .addAllTokens(messageDto.tokens())
            .setNotification(Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build()
            )
            .putData("notificationId", messageDto.notificationId())
            .putData("route", messageDto.route());

        addOptionalData(builder, messageDto);
        return builder.build();
    }

    private void addOptionalData(Message.Builder builder, MessageDto messageDto) {
        if (messageDto.paramKey() != null) {
            builder.putData("paramKey", messageDto.paramKey());
        }
        if (messageDto.buttonType() != null) {
            builder.putData("buttonType", messageDto.buttonType().name());
        }
    }

    private void addOptionalData(MulticastMessage.Builder builder, MessageDto messageDto) {
        if (messageDto.paramKey() != null) {
            builder.putData("paramKey", messageDto.paramKey());
        }
        if (messageDto.buttonType() != null) {
            builder.putData("buttonType", messageDto.buttonType().name());
        }
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
