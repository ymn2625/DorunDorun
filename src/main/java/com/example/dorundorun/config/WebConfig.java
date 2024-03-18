package com.example.dorundorun.config;

import com.siot.IamportRestClient.IamportClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view에서 접근할 경로
    private String savePath = "file:///C:/pg/springboot_img/";  //실제 파일 저장 경로

    // 결제 추가(민지)
    String apiKey = "4262767386671810";
    String secretKey = "rIlHUfYx42cfisrjU4MLqygkchDTRw3QLsoaFvkqaz9sokQPI6p5ruugqMlJH5NI9GDXEzSd9SCvKMPg";
    @Bean
    public IamportClient iamportClient() {

        return new IamportClient(apiKey, secretKey);
    }

    // 추가(민지)
    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(resourcePath)   //  이 경로로 접근하면?
                .addResourceLocations(savePath);    // 이 경로에서 찾아줌
    }
}
