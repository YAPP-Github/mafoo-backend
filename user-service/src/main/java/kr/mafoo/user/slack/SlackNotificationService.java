package kr.mafoo.user.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.TextObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@Service
@RequiredArgsConstructor
public class SlackNotificationService {

    @Value(value = "${slack.webhook.token}")
    private String token;

    @Value(value = "${slack.webhook.channel.error}")
    private String errorChannel;

    @Value(value = "${slack.webhook.channel.member}")
    private String memberChannel;

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

            MethodsClient methods = Slack.getInstance().methods(token);
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

            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException("Can't send Slack Message.", e);
        }
    }

    public void sendNewMemberNotification(String memberId, String memberName, LocalDateTime createdAt) {
        try {
            List<TextObject> textObjects = new ArrayList<>();

            textObjects.add(markdownText(">*새로운 사용자가 가입했습니다!*\n"));
            textObjects.add(markdownText("\n"));

            textObjects.add(markdownText("*사용자 ID:* \n`" + memberId + "`\n"));
            textObjects.add(markdownText("*사용자 이름:* \n`" + memberName + "`\n"));
            textObjects.add(markdownText("*생성일자:* \n`" + createdAt + "`\n"));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest
                    .builder()
                    .channel(memberChannel)
                    .blocks(
                            asBlocks(
                                    divider(),
                                    section(
                                            section -> section.fields(textObjects)
                                    )
                            ))
                    .build();

            methods.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            throw new RuntimeException("Can't send Slack Message.", e);
        }
    }

}
