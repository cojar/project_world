package com.example.world;

import com.example.world.file.FileService;
import com.example.world.file.UploadedFile;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommonUtil {

    private final FileService fileService;

    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public String getFilePath(Long id) {
        UploadedFile file = this.fileService.getFile(id);
        return this.fileService.getFilePath(file);
    }
}