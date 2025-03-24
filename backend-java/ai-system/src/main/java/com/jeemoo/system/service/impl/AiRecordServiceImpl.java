package com.jeemoo.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.entity.SysUser;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeemoo.system.domain.*;
import com.jeemoo.system.mapper.*;
import com.jeemoo.system.param.SendMessageParam;
import com.jeemoo.system.param.VectorData;
import com.jeemoo.system.service.ISysUserService;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jeemoo.system.domain.bo.AiRecordBo;
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.system.service.IAiRecordService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 聊天记录Service业务层处理
 *
 * @author ai
 * @date 2023-09-07
 */
@RequiredArgsConstructor
@Service
public class AiRecordServiceImpl implements IAiRecordService {

    private final AiRecordMapper baseMapper;
    private final AiRecordLikeMapper recordLikeMapper;
    private final AiDocumentMapper documentMapper;
    private final ISysUserService userService;
    private final AiRecordFileMapper recordFileMapper;
    private final AiWindowMapper windowMapper;


    /**
     * 查询聊天记录
     */
    @Override
    public AiRecordVo queryById(Long messageId){
        return baseMapper.selectVoById(messageId);
    }

    /**
     * 查询聊天记录列表
     */
    @Override
    public TableDataInfo<AiRecordVo> queryPageList(AiRecordBo bo, PageQuery pageQuery) {
        if (bo.getWindowId() != null && bo.getWindowId() != 0L) {
            LambdaQueryWrapper<AiRecord> lqw = buildQueryWrapper(bo);
            Page<AiRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
            return TableDataInfo.build(result);
        } else {
            return TableDataInfo.build(new ArrayList<>());
        }
    }

    /**
     * 查询聊天记录列表
     */
    @Override
    public List<AiRecordVo> queryList(AiRecordBo bo) {
        LambdaQueryWrapper<AiRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AiRecord> buildQueryWrapper(AiRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AiRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getWindowId() != null, AiRecord::getWindowId, bo.getWindowId());
        lqw.eq(bo.getUserId() != null, AiRecord::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getRole()), AiRecord::getRole, bo.getRole());
        lqw.eq(bo.getIsInitMessage() != null, AiRecord::getIsInitMessage, bo.getIsInitMessage());
        lqw.eq(bo.getIsLike() != null, AiRecord::getIsLike, bo.getIsLike());
        lqw.eq(StringUtils.isNotBlank(bo.getMasterUuid()), AiRecord::getMasterUuid, bo.getMasterUuid());
        lqw.le(bo.getTimestamp() > 0L,AiRecord::getCreateTimestamp,bo.getTimestamp());
        lqw.eq(bo.getCompanyId() != null && bo.getCompanyId() != 0, AiRecord::getCompanyId, bo.getCompanyId());
        lqw.ne(AiRecord::getCreateTimestamp,0);
        if (bo.isAsc()) {
            lqw.orderByAsc(AiRecord::getCreateTimestamp);
        } else {
            lqw.orderByDesc(AiRecord::getCreateTimestamp);
        }
        return lqw;
    }

    /**
     * 新增聊天记录
     */
    @Override
    public Boolean insertByBo(AiRecordBo bo) {
        AiRecord add = BeanUtil.toBean(bo, AiRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setMessageId(add.getMessageId());
        }
        return flag;
    }

    /**
     * 修改聊天记录
     */
    @Override
    public Boolean updateByBo(AiRecordBo bo) {
        AiRecord update = BeanUtil.toBean(bo, AiRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AiRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除聊天记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<ChatMessage> fetchMessages(AiWindow window, LoginUser loginUser, Integer historyCount) {
        return this.getMessages(window.getWindowId(),loginUser.getUserId(), historyCount);
    }

    @Override
    public List<ChatMessage> fetchMessages(AiWindow window, Long userId, Integer historyCount) {
        return this.getMessages(window.getWindowId(),userId, historyCount);
    }


    private List<ChatMessage> getMessages(Long windowId, Long userId, Integer historyCount) {
        int limit = historyCount != null && historyCount != 0 ? historyCount : 6;
        List<AiRecord> list = baseMapper.selectList(new QueryWrapper<AiRecord>()
                .eq("window_id", windowId)
                .eq("is_init_message",0)
                .eq("user_id", userId)
                .eq("is_error",0)
                .eq("is_context",1)
                .orderBy(true,false,"create_timestamp")
                .last("limit "+limit*2)
        );
        Collections.reverse(list);

        List<ChatMessage> messages = new ArrayList<>();
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> recordIds = new ArrayList<>();
        for (AiRecord aiRecord : list) {
            recordIds.add(aiRecord.getMessageId());
        }
        List<AiRecordFile> recordFiles = recordFileMapper.selectList(new QueryWrapper<AiRecordFile>().in("record_id",recordIds));
        for (AiRecord aiRecord : list) {
            StringBuilder content = new StringBuilder();
            if (!recordFiles.isEmpty()) {
                content.append("以下为文件解析内容请根据以下内容回复：\n");
                for (AiRecordFile recordFile : recordFiles) {
                    if (aiRecord.getMessageId().equals(recordFile.getRecordId())) {
                        content.append(recordFile.getContent()).append("\n");
                    }
                }
            }
            content.append(aiRecord.getContent());
            aiRecord.setContent(content.toString());
            ChatMessage message = new ChatMessage();
            message.setRole(aiRecord.getRole());
            message.setContent(aiRecord.getContent()==null?"":aiRecord.getContent());
            messages.add(message);
        }

        return messages;
    }

    @Override
    public AiRecord insertRecord(SendMessageParam param, AiWindow window, List<VectorData> vectorDataList, Long userId, Long memberId, List<JSONObject> searchList) {
        String masterUuid = UUID.fastUUID().toString();
        String platform = param.getPlatform();
        if (platform == null) {
            platform = "pc";
        }
        List<JSONObject> docInfo = new ArrayList<>();
        if (vectorDataList != null && vectorDataList.size() > 0) {
            Set<Long> ids = new HashSet<>();
            for (VectorData vectorData : vectorDataList) {
                ids.add(vectorData.getDocumentId());
            }
            List<AiDocument> documents = documentMapper.selectList(new QueryWrapper<AiDocument>().in("document_id", ids));
            for (AiDocument document : documents) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("id",document.getDocumentId());
                jsonObject.set("title",document.getDocumentName());
                jsonObject.set("url",document.getUrl());
                docInfo.add(jsonObject);
            }
        }

        String avatar = "";
        SysUser user = userService.selectUserById(userId);
        if (user != null) {
            avatar = user.getAvatar();
        }


        AiRecord recordFile = new AiRecord();
        if (platform.equals("miniapp") && param.getFilesUuid() != null && !param.getFilesUuid().isEmpty()) {
            recordFile.setUserId(userId);
            recordFile.setType("file");
            recordFile.setContent("");
            recordFile.setRole(ChatMessageRole.USER.value());
            recordFile.setWindowId(window.getWindowId());
            recordFile.setPromptId(window.getPromptId());
            recordFile.setCreateTimestamp(System.currentTimeMillis());
            recordFile.setIsInitMessage(0);
            recordFile.setIsLike(0);
            recordFile.setMasterUuid(masterUuid);
            recordFile.setMemberId(memberId);
            recordFile.setCompanyId(window.getCompanyId());
            recordFile.setAvatar(avatar);
            recordFile.setIsError(2);
            baseMapper.insert(recordFile);
        }

        AiRecord nextRecord = new AiRecord();
        nextRecord.setUserId(userId);
        nextRecord.setType("text");
        nextRecord.setRole(ChatMessageRole.ASSISTANT.value());
        nextRecord.setWindowId(window.getWindowId());
        nextRecord.setPromptId(window.getPromptId());
        nextRecord.setMasterUuid(masterUuid);
        nextRecord.setDocInfo(JSONUtil.toJsonStr(docInfo));
        nextRecord.setSearchInfo(JSONUtil.toJsonStr(searchList));
        nextRecord.setMemberId(memberId);
        nextRecord.setCompanyId(window.getCompanyId());
        nextRecord.setAvatar("https://openai-res.jeemoo.com/static/assistant.png");
        nextRecord.setIsError(2);
        baseMapper.insert(nextRecord);

        AiRecord record = new AiRecord();
        record.setNextId(nextRecord.getMessageId());
        record.setUserId(userId);
        record.setType("text");
        record.setContent(param.getMessage());
        record.setRole(ChatMessageRole.USER.value());
        record.setWindowId(window.getWindowId());
        record.setPromptId(window.getPromptId());
        record.setDocInfo(JSONUtil.toJsonStr(docInfo));
        record.setDocInfoList(docInfo);
        record.setSearchInfo(JSONUtil.toJsonStr(searchList));
        record.setSearchInfoList(searchList);
        record.setCreateTimestamp(System.currentTimeMillis());
        record.setIsInitMessage(0);
        record.setIsLike(0);
        record.setMasterUuid(masterUuid);
        record.setMemberId(memberId);
        record.setCompanyId(window.getCompanyId());
        record.setAvatar(avatar);
        record.setIsError(2);
        baseMapper.insert(record);

        if (param.getFilesUuid() != null && !param.getFilesUuid().isEmpty()) {
            if (platform.equals("miniapp")) {
                recordFileMapper.updateRecordId(recordFile.getMessageId(), param.getFilesUuid());
            } else {
                recordFileMapper.updateRecordId(record.getMessageId(), param.getFilesUuid());
            }
        }

//        if (param.getMessage().length() >= 20) {
//            window.setWindowName(param.getMessage().substring(0,20));
//        } else {
//            window.setWindowName(param.getMessage());
//        }
        windowMapper.updateById(window);
        return record;
    }
    @Override
    public AiRecord insertRecord(String message, AiWindow window, List<VectorData> vectorDataList, Long userId) {
        SendMessageParam param = new SendMessageParam();
        param.setMessage(message);
        return this.insertRecord(param,window,vectorDataList,userId,null, null);
    }

    @Override
    public AiRecord insertRecord(String message, AiWindow window, List<VectorData> vectorDataList, Long userId, Long memberId) {
        SendMessageParam param = new SendMessageParam();
        param.setMessage(message);
        return this.insertRecord(param,window,vectorDataList,userId,memberId, null);
    }

    @Override
    public void updateById(AiRecord record) {
        baseMapper.updateById(record);
    }

    @Override
    @Transactional
    public boolean likeOrUnlike(Long messageId, int type, Long userId) {
        if (baseMapper.likeOrUnlike(messageId,type,userId) > 0) {
            AiRecordLike recordLike = new AiRecordLike();
            recordLike.setMessageId(messageId);
            recordLike.setIsLike(type);
            recordLike.setUserId(userId);
            recordLikeMapper.insert(recordLike);
        }

        return true;
    }

    @Override
    public void updateErrorByNextId(Long messageId, Integer isError) {
        baseMapper.updateErrorByNextId(messageId, isError);
    }

    @Override
    public boolean deleteByMasterUuid(LoginUser loginUser, String masterUuid) {
        return baseMapper.deleteByMasterUuid(loginUser.getUserId(),masterUuid) > 0;
    }

    @Override
    public boolean clearContext(AiWindow window) {
        return baseMapper.clearContext(window.getWindowId()) > 0;
    }

    @Override
    public void updateErrorByMasterUuid(String masterUuid, Integer isError) {
        baseMapper.updateErrorByMasterUuid(masterUuid, isError);
    }

    @Override
    public List<AiRecord> list(QueryWrapper<AiRecord> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }
}
