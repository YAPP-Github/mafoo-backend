package kr.mafoo.photo.service;

import kr.mafoo.photo.util.RecapProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Graphics2dService {

    private final RecapProperties recapProperties;

    public Mono<Void> generateAlbumChipForRecap(String recapId, String albumName, String albumType) {

        return Mono.fromCallable(() -> createAlbumChipImage(recapId, albumName, albumType))
                .flatMap(buffer -> ServerResponse.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .bodyValue(buffer))
                .onErrorResume(IOException.class, e -> ServerResponse.status(500)
                        .bodyValue("Error generating album chip image: " + e.getMessage())).then();
    }

    private final Map<String, BufferedImage> iconCache = new HashMap<>();

    private FontMetrics cachedMetrics;

    private String createAlbumChipImage(String recapId, String albumName, String albumType) throws IOException {
        int paddingLeftRight = 32;
        int paddingTopBottom = 22;
        int iconTextSpacing = 8;
        int borderThickness = 2;
        Font font = new Font(recapProperties.getPretendardFontPath(), Font.BOLD, 36);

        FontMetrics metrics = getCachedFontMetrics(font);
        BufferedImage icon = getCachedIcon(albumType);

        int chipHeight = 90;
        int chipWidth = calculateChipWidth(
                icon.getWidth(),
                metrics.stringWidth(albumName),
                paddingLeftRight,
                iconTextSpacing
        );

        // TODO: 이해가 쉬운 형태로 정리 필요
        int[] coordinates = calculateCoordinates(
                chipHeight,
                metrics.getHeight(),
                paddingTopBottom,
                paddingLeftRight,
                icon.getHeight(),
                icon.getWidth(),
                iconTextSpacing,
                metrics.getAscent()
        );

        BufferedImage image = new BufferedImage(chipWidth, chipHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = setupGraphics(image, font);

        drawRoundedRectangle(
                g2d,
                new Color(255, 255, 255, 180),
                chipWidth,
                coordinates[0],
                metrics.getHeight(),
                paddingTopBottom,
                borderThickness,
                chipHeight
        );

        drawIcon(
                g2d,
                icon,
                paddingLeftRight,
                coordinates[1]
        );

        drawText(
                g2d,
                albumName,
                new Color(33, 37, 41),
                coordinates[2],
                coordinates[3]
        );

        g2d.dispose();

        return saveImage(generateAlbumChipImagePath(recapId), image);
    }

    private int calculateChipWidth(int iconWidth, int textWidth, int paddingLeftRight, int iconTextSpacing) {
        return iconWidth + iconTextSpacing + textWidth + paddingLeftRight * 2;
    }

    // TODO: 이해가 쉬운 형태로 정리 필요
    private int[] calculateCoordinates(int imageHeight, int textHeight, int paddingTopBottom, int paddingLeftRight, int iconHeight, int iconWidth, int iconTextSpacing, int ascent) {
        int y = (imageHeight - textHeight - paddingTopBottom * 2) / 2;
        int iconY = y + (textHeight + paddingTopBottom * 2 - iconHeight) / 2;
        int textX = paddingLeftRight + iconWidth + iconTextSpacing;
        int textBaselineY = y + paddingTopBottom + ascent;
        return new int[]{y, iconY, textX, textBaselineY};
    }

    private String generateAlbumChipImagePath(String recapId) {
        return recapProperties.getChipFilePath(recapId);
    }

    private FontMetrics getCachedFontMetrics(Font font) {
        if (cachedMetrics == null) {
            BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D tempGraphics = tempImage.createGraphics();
            tempGraphics.setFont(font);
            cachedMetrics = tempGraphics.getFontMetrics(font);
            tempGraphics.dispose();
        }
        return cachedMetrics;
    }

    private BufferedImage getCachedIcon(String iconType) throws IOException {
        if (!iconCache.containsKey(iconType)) {
            BufferedImage icon = ImageIO.read(new File(recapProperties.getIconPath(iconType)));
            iconCache.put(iconType, icon);
        }
        return iconCache.get(iconType);
    }

    private Graphics2D setupGraphics(BufferedImage image, Font font) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        return g2d;
    }

    private void drawRoundedRectangle(Graphics2D g2d, Color color, int rectangleWidth, int y, int textHeight, int paddingTopBottom, int borderThickness, int imageHeight) {
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.setColor(color);
        g2d.drawRoundRect(borderThickness / 2, y + borderThickness / 2, rectangleWidth - borderThickness, textHeight + paddingTopBottom * 2 - borderThickness, imageHeight, imageHeight);
        g2d.fillRoundRect(borderThickness / 2, y + borderThickness / 2, rectangleWidth - borderThickness, textHeight + paddingTopBottom * 2 - borderThickness, imageHeight, imageHeight);
    }

    private void drawIcon(Graphics2D g2d, BufferedImage icon, int x, int y) {
        g2d.drawImage(icon, x, y, null);
    }

    private void drawText(Graphics2D g2d, String text, Color color, int x, int y) {
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    private String saveImage(String imagePath, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", new File(imagePath));
        return imagePath;
    }

}

