package com.example.springsecurityjwtauthenticationandauthorization.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JwtAuthenticationFilter sınıfı OncePerRequestFilter sınıfından türetilmiştir.
// OncePerRequestFilter sınıfı, bir isteğin bir kez işlenmesini sağlar.
// Bu sınıf, bir isteğin bir kez işlenmesini sağlar.
// @Component anotasyonu, Spring IoC container'ına sınıfın bir bean oluşturulması gerektiğini bildirir.
// @RequiredArgsConstructor sınıfı, sınıfın final alanlarını veya @NonNull anotasyonu ile işaretlenmiş alanlarını parametre olarak alan bir constructor oluşturur.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService; // JwtService sınıfı, JWT işlemlerini gerçekleştiren sınıftır.


    // OncePerRequestFilter sınıfından türetilen sınıfların doFilterInternal() metodu override edilmelidir.
    // Bu metot, bir isteğin bir kez işlenmesini sağlar.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // HttpServletRequest sınıfı, HTTP isteğini temsil eder.
            @NonNull HttpServletResponse response, // HttpServletResponse sınıfı, HTTP yanıtını temsil eder.
            @NonNull FilterChain filterChain // FilterChain sınıfı, bir isteğin bir filtre zincirindeki bir sonraki filtreye gönderilmesini sağlar.
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization"); // HttpServletRequest sınıfının getHeader() metodu, istek başlığını döndürür.
        final String jwtToken; // JWT token'ı tutmak için String tipinde bir değişken tanımlanır.
        final String userEmail; // Emaili tutmak için String tipinde bir değişken tanımlanır.


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //
            return;
        }
        jwtToken = authorizationHeader.substring(7); // String tipindeki değişkenin değeri, authorizationHeader değişkeninin 7. karakterinden itibaren alınır.
        userEmail = jwtService.extractUsername(jwtToken); // String tipindeki değişkenin değeri, jwtService sınıfının extractEmail() metodu ile alınır.

    }
}
