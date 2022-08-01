package ismail.coding.todoappspring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Data
public class ImageModel {

    private  int id ;
    private String name  ;
    private String type ;
    private byte[] image ;
    private int taskId ;

    public ImageModel(String name, String type, byte[] image) {
        this.name = name;
        this.type = type;
        this.image = image;
    }

    public static ImageModel generateImageModel(MultipartFile image) throws IOException {
            //

            return new ImageModel(image.getName(),image.getContentType(),image.getBytes()) ;

    }
}
