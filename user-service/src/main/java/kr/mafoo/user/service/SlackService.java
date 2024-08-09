package kr.mafoo.user.service;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.TextObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value(value = "${slack.webhook.channel.error}")
    private String errorChannel;

    @Value(value = "${slack.webhook.channel.member}")
    private String memberChannel;

    private final MethodsClient methodsClient;

    public void sendErrorNotification(Throwable throwable, String method, String uri, String statusCode, long executionTime, String userAgent) {
        try {
            List<TextObject> textObjects = new ArrayList<>();

            textObjects.add(markdownText(">*예상하지 못한 에러가 발생했습니다!*\n"));
            textObjects.add(markdownText("\n"));

            textObjects.add(markdownText("*메소드:* \n`" + method + "`\n"));
            textObjects.add(markdownText("*URI:* \n`" + uri + "`\n"));
            textObjects.add(markdownText("*상태코드:* \n`" + statusCode + "`\n"));
            textObjects.add(markdownText("*메세지:* \n`" + throwable.getMessage() + "`\n"));
            textObjects.add(markdownText("*소요시간:* \n`" + executionTime + " ms`\n"));
            textObjects.add(markdownText("*사용자:* \n`" + userAgent + "`\n"));

            ChatPostMessageRequest request = ChatPostMessageRequest
                    .builder()
                    .channel(errorChannel)
                    .blocks(
                            asBlocks(
                                    divider(),
                                    section(
                                            section -> section.fields(textObjects)
                                    )
                            ))
                    .build();

            methodsClient.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException("Can't send Slack Message.", e);
        }
    }

    public Mono<Void> sendNewMemberNotification(String memberId, String memberName, String memberProfileImageUrl, String memberCreatedAt, String userAgent) {
        return Mono.fromCallable(() -> {
                    List<LayoutBlock> layoutBlocks = new ArrayList<>();

                    layoutBlocks.add(
                            Blocks.header(
                                    headerBlockBuilder ->
                                            headerBlockBuilder.text(plainText("🎉 신규 사용자 가입"))));
                    layoutBlocks.add(divider());

                    MarkdownTextObject userIdMarkdown =
                            MarkdownTextObject.builder().text("`사용자 ID`\n" + memberId).build();

                    MarkdownTextObject userNameMarkdown =
                            MarkdownTextObject.builder().text("`사용자 닉네임`\n" + memberName).build();

                    layoutBlocks.add(
                            section(
                                    section -> section.fields(List.of(userIdMarkdown, userNameMarkdown))));

                    MarkdownTextObject userProfileImageMarkdown =
                            MarkdownTextObject.builder().text("`프로필 이미지`\n" + memberProfileImageUrl).build();

                    MarkdownTextObject userCreatedAtMarkdown =
                            MarkdownTextObject.builder().text("`가입 일자`\n" + memberCreatedAt).build();

                    layoutBlocks.add(
                            section(
                                    section -> section.fields(List.of(userProfileImageMarkdown, userCreatedAtMarkdown))));

                    MarkdownTextObject userUserAgentMarkdown =
                            MarkdownTextObject.builder().text("`가입 환경`\n" + userAgent).build();

                    layoutBlocks.add(
                            section(
                                    section -> section.fields(List.of(userUserAgentMarkdown))));

                    ChatPostMessageRequest chatPostMessageRequest =
                            ChatPostMessageRequest
                                    .builder()
                                    .text("신규 사용자 가입 알림")
                                    .channel(memberChannel)
                                    .blocks(layoutBlocks)
                                    .build();

                    return methodsClient.chatPostMessage(chatPostMessageRequest);
                })
                .then();
    }

}
