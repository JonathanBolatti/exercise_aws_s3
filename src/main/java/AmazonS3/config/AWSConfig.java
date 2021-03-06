package AmazonS3.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${ACCESS_KEY}")
    private String accessKey;
    @Value("${SECRET_KEY}")
    private String secretKey;
    @Value("${S3_REGION}")
    private String s3Region;

    @Bean
    public AmazonS3 initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(s3Region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}
