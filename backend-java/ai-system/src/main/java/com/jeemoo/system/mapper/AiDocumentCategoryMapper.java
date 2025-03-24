package com.jeemoo.system.mapper;

import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.system.domain.AiDocumentCategory;
import com.jeemoo.system.domain.bo.AiDocumentCategoryBo;
import com.jeemoo.system.domain.vo.AiDocumentCategoryVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.system.param.MenuTree;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 知识库分类Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiDocumentCategoryMapper extends BaseMapperPlus<AiDocumentCategoryMapper, AiDocumentCategory, AiDocumentCategoryVo> {


    List<MenuTree> menuTree(@Param("bo") AiDocumentCategoryBo bo,@Param("categoryIds") List<Long> categoryIds);

    @Select("select * from ai_document_category where deleted = 0 and company_id = #{bo.companyId}")
    List<AiDocumentCategoryVo> tree(@Param("bo") AiDocumentCategoryBo bo);

    @Delete("delete from ai_document_category_role where category_id = #{categoryId}")
    int deleteRole(@Param("categoryId") Long categoryId);

    int setRole(@Param("categoryId") Long categoryId,@Param("roleIds") List<Long> roleIds);

    List<SysRole> getRole(@Param("categoryId") Long categoryId);

    List<AiDocumentCategoryVo> treeByRole(@Param("bo") AiDocumentCategoryBo bo,@Param("userId") Long userId);

    List<MenuTree> myMenuTree(@Param("bo") AiDocumentCategoryBo bo);

    int updatePathByCompanyId(@Param("companyId") Long companyId);
}
