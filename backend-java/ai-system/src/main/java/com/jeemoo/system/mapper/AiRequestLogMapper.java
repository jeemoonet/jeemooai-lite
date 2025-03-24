package com.jeemoo.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.system.domain.AiRequestLog;
import com.jeemoo.system.domain.bo.AiRequestLogBo;
import com.jeemoo.system.domain.vo.AiRequestLogVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 请求记录Mapper接口
 *
 * @author ai
 * @date 2023-09-12
 */
public interface AiRequestLogMapper extends BaseMapperPlus<AiRequestLogMapper, AiRequestLog, AiRequestLogVo> {

    Page<AiRequestLogVo> listPage(@Param("page") Page<Object> build,@Param("bo") AiRequestLogBo bo);

    Page<AiRequestLogVo> page(@Param("page") Page<Object> page,@Param("param") AiRequestLogBo param);

    void setIntegral(@Param("uuid") String uuid,@Param("integral") Integer integral);

    List<AiRequestLogVo> list(@Param("bo") AiRequestLogBo bo);
}
