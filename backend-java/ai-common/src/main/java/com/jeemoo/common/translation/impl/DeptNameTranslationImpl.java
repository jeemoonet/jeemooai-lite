package com.jeemoo.common.translation.impl;

import com.jeemoo.common.annotation.TranslationType;
import com.jeemoo.common.constant.TransConstant;
import com.jeemoo.common.core.service.DeptService;
import com.jeemoo.common.translation.TranslationInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 部门翻译实现
 *
 * @author Lion Li
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.DEPT_ID_TO_NAME)
public class DeptNameTranslationImpl implements TranslationInterface<String> {

    private final DeptService deptService;
    
    @Override
    public String translation(Object key, String other) {
        return deptService.selectDeptNameByIds(key.toString());
    }
}
