package ismail.coding.todoappspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.*;

@AllArgsConstructor
@Data
public class ImageModel {

    private  Long id ;
    private String name  ;
    private String type ;
    private byte[] imageBytes ;
    private Long taskId ;

    public ImageModel(String name, String type, byte[] image) {
        this.name = name;
        this.type = type;
        this.imageBytes = image;
    }

    public static ImageModel generateImageModel(MultipartFile file) throws IOException ,IllegalStateException{
        isFileEmpty(file);
        isFileImage(file);
        return new ImageModel(file.getName(),file.getContentType(),file.getBytes()) ;

    }

    private static void isFileImage(MultipartFile file) {
        String type = file.getContentType();
        if ( List.of(IMAGE_JPEG,IMAGE_PNG,IMAGE_GIF).contains(type)) {
                 throw new  IllegalStateException("File must be image") ;
             }
    }

    private static void isFileEmpty(MultipartFile file) {
        if ( file.isEmpty() ) throw new  IllegalStateException("File Empty Size = "+ file.getSize()) ;
    }
}
