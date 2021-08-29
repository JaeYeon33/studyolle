package com.jaeyeon.studyolle;

import com.jaeyeon.studyolle.account.account.AccountService;
import com.jaeyeon.studyolle.account.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {

        String nickname = withAccount.value();
        String password = "12345678";
        String email = nickname + "@gmail.com";

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail(email);
        signUpForm.setPassword(password);
        signUpForm.setNickname(nickname);
        accountService.processNewAccount(signUpForm);

        // 이게 원래 `@UserDetails` 애노테이션이 하던 일이다.
        // Authentication 만들기 및 SecurityContext 에 넣어주기
        UserDetails principal = accountService.loadUserByUsername(nickname);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                principal.getPassword(), principal.getAuthorities());
        // 빈 시큐리티 컨텍스트 만들고, 거기에 AuthenticationToken 넣기
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;



//        SignUpForm signUpForm = new SignUpForm();
//        signUpForm.setNickname(nickname);
//        signUpForm.setEmail(nickname + "@gmail.com");
//        signUpForm.setPassword("12345678");
//        accountService.processNewAccount(signUpForm);
//
//        UserDetails principal = accountService.loadUserByUsername(nickname);
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//        return context;
    }
}
