package fr.cyu.schoolmanagementsystem.config;

import fr.cyu.schoolmanagementsystem.component.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/login", "/index", "/register", "/auth", "/") // Exclure les pages publiques
                .excludePathPatterns("/css/**", "/js/**", "/images/**") // Exclure les ressources statiques
                .addPathPatterns("/**"); // Applique l'intercepteur à toutes les autres pages
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/jsp/", ".jsp");
    }




}
