package kr.mafoo.user.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FcmConfig {

    @Value("${cloud.firebase}")
    private String firebaseSecret;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        ByteArrayInputStream credentials = new ByteArrayInputStream(Base64.getDecoder().decode(firebaseSecret));

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(credentials))
            .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
