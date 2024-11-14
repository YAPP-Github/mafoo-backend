package kr.mafoo.photo.service.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "recap")
public class RecapProperties {

    @Value("${recap.path.tmp}")
    private String tmpPath;

    @Value("${recap.path.src}")
    private String srcPath;

    public String getDownloadFilePath(String identifier, int index) {
        return String.format("%s%s_download_%02d.png", tmpPath, identifier, index);
    }

    public String getPhotoFilePath(String identifier) {
        return String.format("%s%s_photo_%%02d.png", tmpPath, identifier);
    }

    public String getPhotoFilePath(String identifier, int index) {
        return String.format("%s%s_photo_%02d.png", tmpPath, identifier, index);
    }

    public String getVideoFilePath(String identifier) {
        return String.format("%s%s_video.mp4", tmpPath, identifier);
    }

    public String getChipFilePath(String identifier) {
        return String.format("%s%s_chip.png", tmpPath, identifier);
    }

    public String getFrameFilePath(String identifier) {
        return String.format("%s%s_frame.png", tmpPath, identifier);
    }

    public String getBackgroundPath(String identifier) {
        return String.format("%sbackground/%s.png", srcPath, identifier);
    }

    public String getIconPath(String identifier) {
        return String.format("%sicon/%s.png", srcPath, identifier);
    }

    public String getAggroMFontPath() {
        return String.format("%sfont/SB_AggroOTF_M.otf", srcPath);
    }

    public String getAggroBFontPath() {
        return String.format("%sfont/SB_AggroOTF_B.otf", srcPath);
    }

    public String getPretendardFontPath() {
        return String.format("%sfont/Pretendard-SemiBold.otf", srcPath);
    }
}

