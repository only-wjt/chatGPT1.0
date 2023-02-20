package com.onlyWjt.socket.controller;

import com.onlyWjt.socket.bean.MsgInfo;
import com.onlyWjt.socket.bean.SessionList;
import com.onlyWjt.socket.common.bean.AjaxResult;
import com.onlyWjt.socket.mapper.MsgInfoMapper;
import com.onlyWjt.socket.mapper.SeesionListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MsgInfoController {

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    @Autowired
    private SeesionListMapper seesionListMapper;

    // 消息列表
    @GetMapping("/msgList")
    public AjaxResult<?> msgList(@RequestParam Integer sessionId){
        SessionList sessionList = seesionListMapper.selectByPrimaryKey(sessionId);
        if(sessionList == null){
            return AjaxResult.success();
        }
        Integer fromUserId = sessionList.getUserId();
        Integer toUserId = sessionList.getToUserId();
        List<MsgInfo> msgInfoList = msgInfoMapper.selectMsgList(fromUserId,toUserId);
        // 更新消息已读
        msgInfoMapper.msgRead(fromUserId, toUserId);
        // 更新会话里面的未读消息
        seesionListMapper.delUnReadCount(fromUserId, toUserId);
        return AjaxResult.success(msgInfoList);
    }


}
