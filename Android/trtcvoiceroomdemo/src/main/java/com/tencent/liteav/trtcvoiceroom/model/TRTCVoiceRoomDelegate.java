package com.tencent.liteav.trtcvoiceroom.model;

import com.tencent.liteav.trtcvoiceroom.model.TRTCVoiceRoomDef.SeatInfo;
import com.tencent.trtc.TRTCCloudDef;

import java.util.ArrayList;
import java.util.List;

public interface TRTCVoiceRoomDelegate {
    /**
     * Callback for error
     * @param code Error code
     * @param message Error message
     */
    void onError(int code, String message);

    /**
     * Callback for warning
     * @param code Warning code
     * @param message Warning message
     */
    void onWarning(int code, String message);

    /**
     * Debugging log
     * @param message Message
     */
    void onDebugLog(String message);

    /**
     * Callback for room termination
     * @param roomId Termination roomid
     */
    void onRoomDestroy(String roomId);

    /**
     * Callback for room information change
     * @param roomInfo Room information
     */
    void onRoomInfoChange(TRTCVoiceRoomDef.RoomInfo roomInfo);

    /**
     * Callback for room seat change
     * @param seatInfoList Seat list information
     */
    void onSeatListChange(List<SeatInfo> seatInfoList);

    /**
     * Callback for anchor mic-on
     * @param index Seat number
     * @param user User information
     */
    void onAnchorEnterSeat(int index, TRTCVoiceRoomDef.UserInfo user);

    /**
     * Callback for anchor mic-off
     * @param index Seat number
     * @param user User information
     */
    void onAnchorLeaveSeat(int index, TRTCVoiceRoomDef.UserInfo user);

    /**
     * Callback for seat mute status
     * @param index Seat number
     * @param isMute Mute status
     */
    void onSeatMute(int index, boolean isMute);

    /**
     * Callback for seat closure
     * @param index Seat number
     * @param isClose Whether it is closed
     */
    void onSeatClose(int index, boolean isClose);

    /**
     * Callback for viewer's room entry
     * @param userInfo Viewer information
     */
    void onAudienceEnter(TRTCVoiceRoomDef.UserInfo userInfo);

    /**
     * Callback for viewer's room exit
     * @param userInfo Viewer information
     */
    void onAudienceExit(TRTCVoiceRoomDef.UserInfo userInfo);

    /**
     * Callback for user volume change
     * @param userVolumes user voice volume list
     * @param totalVolume total volume
     */
    void onUserVolumeUpdate(ArrayList<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume);

    /**
     * Callback for text message receipt
     * @param message Message content
     * @param userInfo Sender information
     */
    void onRecvRoomTextMsg(String message, TRTCVoiceRoomDef.UserInfo userInfo);

    /**
     * Callback for custom message (command message) receipt
     * @param cmd Command
     * @param message Message content
     * @param userInfo Sender information
     */
    void onRecvRoomCustomMsg(String cmd, String message, TRTCVoiceRoomDef.UserInfo userInfo);

    /**
     * Callback for invitation message receipt
     * @param inviteID invite ID
     * @param inviter Inviter ID
     * @param cmd Command
     * @param content Content
     */
    void onReceiveNewInvitation(String inviteID, String inviter, String cmd, String content);

    /**
     * Callback for invitation acceptance
     * @param inviteID invite ID
     * @param invitee invitee ID
     */
    void onInviteeAccepted(String inviteID, String invitee);

    /**
     * Callback for invitation decline
     * @param inviteID invite ID
     * @param invitee invitee ID
     */
    void onInviteeRejected(String inviteID, String invitee);

    /**
     * Callback for invitation cancellation
     * @param inviteID invite ID
     * @param inviter Inviter ID
     */
    void onInvitationCancelled(String inviteID, String inviter);
}
