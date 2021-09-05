package com.jaeyeon.studyolle.modules.account.validator;


import com.jaeyeon.studyolle.modules.account.AccountRepository;
import com.jaeyeon.studyolle.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aclass) {
        return aclass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
//      SignUpForm signUpForm = (SignUpForm) errors; 에러발생
//      errors가 아닌 첫번째 파라미터(Object)를 SignUpForm으로 변환
        SignUpForm signUpForm = (SignUpForm) object;

        if (accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email", "invalid.email"
                    , new Object[]{signUpForm.getEmail()}, "이메일이 이미 존재합니다");
        }

        if (accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname", "invalid.nickname"
                    , new Object[]{signUpForm.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
