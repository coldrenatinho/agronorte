package com.agronorte.application.usecase;

import com.agronorte.application.dto.AuthDTO;
import com.agronorte.application.port.out.UserRepository;
import com.agronorte.domain.entity.User;
import com.agronorte.domain.exception.BusinessRuleException;
import com.agronorte.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case de Autenticação. Orquestra domínio + ports. Não sabe nada de HTTP,
 * não sabe nada de JPA.
 * Quando precisar trocar JWT por OAuth, só mexo aqui e no JwtService. Zero
 * impacto no domínio.
 */
@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("E-mail já cadastrado: " + request.email());
        }

        var user = User.create(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role());

        var savedUser = userRepository.save(user);
        var token = jwtService.generateToken(savedUser.getEmailValue(), savedUser.getRole().name());

        return AuthDTO.AuthResponse.of(token, savedUser.getName(),
                savedUser.getEmailValue(), savedUser.getRole());
    }

    @Transactional(readOnly = true)
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessRuleException("Credenciais inválidas."));

        if (!user.isActive()) {
            throw new BusinessRuleException("Usuário inativo. Entre em contato com o suporte.");
        }

        var token = jwtService.generateToken(user.getEmailValue(), user.getRole().name());
        return AuthDTO.AuthResponse.of(token, user.getName(),
                user.getEmailValue(), user.getRole());
    }
}
