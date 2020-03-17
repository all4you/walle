package com.ngnis.walle.common.bean;

import cn.hutool.core.text.StrFormatter;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import javax.validation.*;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验Bean是否合法
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
public class BeanValidator {

    private static Validator classValidator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 国际化，强制用英文，ref：https://docs.oracle.com/javase/tutorial/i18n/locale/create.html
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        // 国际化，指定语言，ref：https://www.ibm.com/developerworks/cn/java/j-cn-hibernate-validator/index.html
        MessageInterpolator interpolator = new LocalizedMessageInterpolator(validatorFactory.getMessageInterpolator(), locale);
        classValidator = validatorFactory.usingContext().messageInterpolator(interpolator).getValidator();
    }


    /**
     * 验证失败时抛出ConstraintViolationException
     */
    public static BaseResult validate(Object object, Class<?>... groups) throws ConstraintViolationException {
        BaseResult baseResult = new BaseResult();
        // 校验
        Set<ConstraintViolation<Object>> constraintViolations = classValidator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            String messages = String.join(";", constraintViolations.stream()
                    .filter(t -> t instanceof ConstraintViolationImpl)
                    .map(t -> {
                        ConstraintViolationImpl t1 = (ConstraintViolationImpl) t;
                        return StrFormatter.format("字段:{}不合法,原因:{}", t1.getPropertyPath(), t1.getMessage());
                    })
                    .collect(Collectors.toList()));
            baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), messages);
        }
        return baseResult;
    }


}