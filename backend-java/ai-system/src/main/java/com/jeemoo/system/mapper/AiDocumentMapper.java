package com.jeemoo.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiDocument;
import com.jeemoo.system.domain.vo.AiDocumentVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.system.param.DocumentSearchParam;
import com.jeemoo.system.param.SplitDocumentParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 文档Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiDocumentMapper extends BaseMapperPlus<AiDocumentMapper, AiDocument, AiDocumentVo> {

    List<AiDocumentVo> apiPage(@Param("param") DocumentSearchParam param,
                               @Param("page") Page<Object> page,
                               @Param("loginUser") LoginUser loginUser);

    @Update("update ai_document set status = #{status} where document_id = #{documentId}")
    int setStatus(@Param("documentId") Long documentId,@Param("status") int status);

    @Update("update ai_document set status = 0,split_id=#{param.id},length=#{param.length},split_char=#{param.splitChar} where document_id = #{documentId}")
    int split(@Param("documentId") Long documentId,@Param("param") SplitDocumentParam param);
}
