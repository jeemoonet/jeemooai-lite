package com.jeemoo.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.system.domain.AiRecommendQuestion;
import com.jeemoo.system.domain.vo.AiRecommendQuestionVo;
import com.jeemoo.system.domain.bo.AiRecommendQuestionBo;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 推荐问题Service接口
 *
 * @author ai
 * @date 2024-05-29
 */
public interface IAiRecommendQuestionService {

    /**
     * 查询推荐问题
     */
    AiRecommendQuestionVo queryById(Long questionId);

    /**
     * 查询推荐问题列表
     */
    TableDataInfo<AiRecommendQuestionVo> queryPageList(AiRecommendQuestionBo bo, PageQuery pageQuery);

    /**
     * 查询推荐问题列表
     */
    List<AiRecommendQuestionVo> queryList(AiRecommendQuestionBo bo);

    /**
     * 新增推荐问题
     */
    Boolean insertByBo(AiRecommendQuestionBo bo);

    /**
     * 修改推荐问题
     */
    Boolean updateByBo(AiRecommendQuestionBo bo);

    /**
     * 校验并批量删除推荐问题信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<AiRecommendQuestion> list(QueryWrapper<AiRecommendQuestion> queryWrapper);
}
