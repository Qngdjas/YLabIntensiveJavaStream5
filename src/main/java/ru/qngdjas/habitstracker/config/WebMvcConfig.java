package ru.qngdjas.habitstracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.qngdjas.habitstracker.api.handler.AuthInterceptor;

import java.util.List;

/**
 * MVC конфигурация API.
 */
@Configuration
@EnableWebMvc
@ComponentScan("ru.qngdjas.habitstracker")
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Расширение конвертеров сообщений:
     * <p>В частности: настройка маппинга JSON через Jackson.
     *
     * @param converters Список преобразователей.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    /**
     * Регистрация request-перехватчиков.
     * <p>В частности: перехватчик действительной Http-сессии.
     *
     * @param registry Контейнер перехватчиков.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register");
    }
}
