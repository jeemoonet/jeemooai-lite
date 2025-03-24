package com.jeemoo.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.domain.vo.AiWindowVo;
import com.jeemoo.common.core.mapper.BaseMapperPlus;
import com.jeemoo.system.param.CompanyWindowParam;
import com.jeemoo.system.param.CompanyWindowResponse;
import com.jeemoo.system.param.RenameWindowParam;
import com.jeemoo.system.param.WindowListResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 窗口Mapper接口
 *
 * @author ai
 * @date 2023-09-07
 */
public interface AiWindowMapper extends BaseMapperPlus<AiWindowMapper, AiWindow, AiWindowVo> {

    List<WindowListResponse> listByUser(@Param("loginUser") LoginUser loginUser, @Param("talkType") int talkType,@Param("platform") String platform, @Param("filterWindowId") Long filterWindowId);

    @Update("update ai_window set window_name = #{param.windowName} where window_id = #{param.windowId} and user_id = #{loginUser.userId}")
    int rename(@Param("param") RenameWindowParam param,@Param("loginUser") LoginUser loginUser);

    Page<CompanyWindowResponse> pageByCompany(@Param("page") Page<Object> page,
                                              @Param("param") CompanyWindowParam param,
                                              @Param("loginUser") LoginUser loginUser);

    Long findOneFreeWindow(@Param("userId") Long userId, @Param("platform") String platform, @Param("promptId") Long promptId);
}
