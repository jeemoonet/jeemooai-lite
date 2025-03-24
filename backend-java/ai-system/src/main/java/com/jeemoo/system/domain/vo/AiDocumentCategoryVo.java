package com.jeemoo.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeemoo.system.param.MenuTree;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 知识库分类视图对象 ai_document_category
 *
 * @author ai
 * @date 2023-09-07
 */
@Data
@ExcelIgnoreUnannotated
public class AiDocumentCategoryVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long categoryId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String categoryName;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String categoryIcon;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long parentId;

    private String path;

    private List<AiDocumentCategoryVo> children;

    private Date createTime;

    private Date updateTime;
    /**
     *
     */
    @JsonIgnore
    private Integer deleted;

    @JsonIgnore
    private Long companyId;

    private Long userId;

    private Integer isMy;

    private boolean disabled = false;

    public static List<AiDocumentCategoryVo> buildCategoryTree(List<AiDocumentCategoryVo> categories) {
        List<AiDocumentCategoryVo> tree = new ArrayList<>();

        for (AiDocumentCategoryVo category : categories) {
            boolean isParentExist = false;
            if (category.getParentId() == 0) {
                tree.add(category);
                isParentExist = true;
            } else {
                for (AiDocumentCategoryVo parentCategory : categories) {
                    if (parentCategory.getCategoryId().equals(category.getParentId())) {
                        parentCategory.addChild(category);
                        isParentExist = true;
                        break;
                    }
                }
            }

            if (!isParentExist) {
                tree.add(category);
            }
        }

        return tree;
    }

    public void addChild(AiDocumentCategoryVo child) {
        if (children == null) children = new ArrayList<>();
        children.add(child);
    }
}
