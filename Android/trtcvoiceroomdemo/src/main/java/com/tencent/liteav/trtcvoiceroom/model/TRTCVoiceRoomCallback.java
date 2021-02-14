package com.tencent.liteav.trtcvoiceroom.model;

import java.util.List;

public class TRTCVoiceRoomCallback {
    /**
     * basic callback
     */
    public interface ActionCallback {
        void onCallback(int code, String msg);
    }

    /**
     * room infos callback
     */
    public interface RoomInfoCallback {
        void onCallback(int code, String msg, List<TRTCVoiceRoomDef.RoomInfo> list);
    }

    /**
     * 获取成员信息回调
     */
    public interface UserListCallback {
        void onCallback(int code, String msg, List<TRTCVoiceRoomDef.UserInfo> list);
    }
}
