package com.jeemoo.system.mapper;

import com.jeemoo.system.domain.AiDocumentEntity;
import com.jeemoo.system.domain.vo.AiDocumentEntityVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档向量关系Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiDocumentEntityMapper extends BaseMapperPlus<AiDocumentEntityMapper, AiDocumentEntity, AiDocumentEntityVo> {

    List<Long> getEntityIds(@Param("documentIds") List<Long> documentIds);
}
