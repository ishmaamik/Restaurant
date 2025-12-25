package app.SupabaseStorage;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class StorageService {

    @Value("${supabase.service-key}")
    private String ServiceKey;

    @Value("${spring.bucket-url}")
    private String SupabaseUrl;

    @Value("${spring.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file){
        try{
            String fileName= System.currentTimeMillis() + "-" +
                    file.getOriginalFilename().replaceAll("\\s+", "_");

            OkHttpClient client= new OkHttpClient();

            RequestBody fileBody = RequestBody.create(
                    file.getBytes(),
                    MediaType.parse(file.getContentType())
            );

            Request request= new Request.Builder()
                    .url(SupabaseUrl + "/storage/v1/object/" + bucket + "/" +fileName)
                    .addHeader("Authorization", "Bearer "+ServiceKey)
                    .addHeader("apiKey", ServiceKey)
                    .post(fileBody)
                    .build();

            Response response= client.newCall(request).execute();

            if(!response.isSuccessful()){
                throw new RuntimeException("Supabase upload failed: " + response.body().string());
            }

            System.out.println(SupabaseUrl+"/storage/v1/object/"+bucket+"/"+fileName);
            return SupabaseUrl+"/storage/v1/object/"+bucket+"/"+fileName;
        }
        catch(IOException e){
            throw new RuntimeException("File upload error", e);
        }

    }
}
