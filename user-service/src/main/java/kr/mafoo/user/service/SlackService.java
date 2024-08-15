package kr.mafoo.user.service;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value(value = "${slack.webhook.channel.error}")
    private String errorChannel;

    @Value(value = "${slack.webhook.channel.member}")
    private String memberChannel;

    private final MethodsClient methodsClient;

    public Mono<Void> sendErrorNotification(String method, String uri, String originIp, String userAgent, String message) {
        return Mono.fromCallable(() -> {
            List<LayoutBlock> layoutBlocks = new ArrayList<>();

            // Header 삽입
            layoutBlocks.add(
                    Blocks.header(
                            headerBlockBuilder ->
                                    headerBlockBuilder.text(plainText("🚨 예상하지 못한 에러 발생"))
                    )
            );

            layoutBlocks.add(divider());

            // Content 삽입
            MarkdownTextObject errorMethodMarkdown =
                    MarkdownTextObject.builder().text("`METHOD`\n" + method).build();

            MarkdownTextObject errorUriMarkdown =
                    MarkdownTextObject.builder().text("`URI`\n" + uri).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(errorMethodMarkdown, errorUriMarkdown))
                    )
            );

            MarkdownTextObject errorOriginIpMarkdown =
                    MarkdownTextObject.builder().text("`에러 발생 IP`\n" + originIp).build();

            MarkdownTextObject errorUserAgentMarkdown =
                    MarkdownTextObject.builder().text("`에러 발생 환경`\n" + userAgent).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(errorOriginIpMarkdown, errorUserAgentMarkdown))
                    )
            );

            MarkdownTextObject errorMessageMarkdown =
                    MarkdownTextObject.builder().text("`메세지`\n" + message).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(errorMessageMarkdown))
                    )
            );

            ChatPostMessageRequest chatPostMessageRequest =
                    ChatPostMessageRequest
                            .builder()
                            .text("예상하지 못한 에러 발생 알림")
                            .channel(errorChannel)
                            .blocks(layoutBlocks)
                            .build();

            return methodsClient.chatPostMessage(chatPostMessageRequest);

        }).then();
    }

    public Mono<Void> sendNewMemberNotification(String memberId, String memberName, String memberProfileImageUrl, String memberCreatedAt, String userAgent) {
        return Mono.fromCallable(() -> {
            List<LayoutBlock> layoutBlocks = new ArrayList<>();

            // Header 삽입
            layoutBlocks.add(
                    Blocks.header(
                            headerBlockBuilder ->
                                    headerBlockBuilder.text(plainText("🎉 신규 사용자 가입"))
                    )
            );

            layoutBlocks.add(divider());

            // Content 삽입
            MarkdownTextObject userIdMarkdown =
                    MarkdownTextObject.builder().text("`사용자 ID`\n" + memberId).build();

            MarkdownTextObject userNameMarkdown =
                    MarkdownTextObject.builder().text("`사용자 닉네임`\n" + memberName).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(userIdMarkdown, userNameMarkdown))
                    )
            );

            MarkdownTextObject userProfileImageMarkdown =
                    MarkdownTextObject.builder().text("`프로필 이미지`\n" + memberProfileImageUrl).build();

            MarkdownTextObject userCreatedAtMarkdown =
                    MarkdownTextObject.builder().text("`가입 일자`\n" + memberCreatedAt).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(userProfileImageMarkdown, userCreatedAtMarkdown))
                    )
            );

            MarkdownTextObject userUserAgentMarkdown =
                    MarkdownTextObject.builder().text("`가입 환경`\n" + userAgent).build();

            layoutBlocks.add(
                    section(
                            section -> section.fields(List.of(userUserAgentMarkdown))
                    )
            );

            ChatPostMessageRequest chatPostMessageRequest =
                    ChatPostMessageRequest
                            .builder()
                            .text("신규 사용자 가입 알림")
                            .channel(memberChannel)
                            .blocks(layoutBlocks)
                            .build();

            return methodsClient.chatPostMessage(chatPostMessageRequest);

        }).then();
    }

}
