package com.tencent.liteav.trtcvoiceroom.model;

import android.content.Context;
import android.os.Handler;

import com.tencent.liteav.audio.TXAudioEffectManager;
import com.tencent.liteav.trtcvoiceroom.model.impl.TRTCVoiceRoomImpl;

import java.util.List;

public abstract class TRTCVoiceRoom {

    /**
     * Get the `TRTCVoiceRoom` singleton object
     *
     * @param context Android context
     * @return TRTCVoiceRoom instance
     * @note  {@link TRTCVoiceRoom#destroySharedInstance()} can be called to terminate a singleton object
     */
    public static synchronized TRTCVoiceRoom sharedInstance(Context context) {
        return TRTCVoiceRoomImpl.sharedInstance(context);
    }

    /**
     * Terminate the `TRTCVoiceRoom` singleton object
     *
     * @note - note: after the instance is terminated, the externally cached `TRTCVoiceRoom` instance cannot be reused
     *               and you need to call `{@link TRTCVoiceRoom#sharedInstance(Context)}` again to get a new instance
     */
    public static void destroySharedInstance() {
        TRTCVoiceRoomImpl.destroySharedInstance();
    }

    //////////////////////////////////////////////////////////
    //
    //                 Basic Apis
    //
    //////////////////////////////////////////////////////////
    /**
     * Set the component callback
     * <p>
     * You can use `TRTCVoiceRoomDelegate` to get various status notifications of `TRTCVoiceRoom`
     *
     * @param delegate Callback
     * @note callback events in `TRTCVoiceRoom` are called back to you in the main queue by default.
     *       If you need to specify a thread for event callback, please use `{@link TRTCVoiceRoom#setDelegateHandler(Handler)}`
     */
    public abstract void setDelegate(TRTCVoiceRoomDelegate delegate);

    /**
     * Set the thread handler where the event callback is
     *
     * @param handler Various status callback notifications in `TRTCVoiceRoom` will be sent to the handler you specify.
     */
    public abstract void setDelegateHandler(Handler handler);

    /**
     * Log in
     *
     * @param sdkAppId You can view the `SDKAppID` in the TRTC console > **[Application Management](https://console.cloud.tencent.com/trtc/app)** > "Application Information"
     * @param userId ID of the current user, which is a string that can contain only letters (a–z and A–Z), digits (0–9), hyphens (-), and underscores (_)
     * @param userSig Tencent Cloud's proprietary security protection signature. For more information on how to get it, please see [UserSig](https://cloud.tencent.com/document/product/647/17275).
     * @param callback Login callback. The `code` will be 0 if login is successful
     */
    public abstract void login(int sdkAppId, String userId, String userSig, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Log off
     */
    public abstract void logout(TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Set user information. The user information you set will be stored in Tencent Cloud IM.
     *
     * @param userName User nickname, which cannot be nil
     * @param avatarURL User profile photo
     * @param callback Result callback for whether the setting succeeds
     */
    public abstract void setSelfProfile(String userName, String avatarURL, TRTCVoiceRoomCallback.ActionCallback callback);

    //////////////////////////////////////////////////////////
    //
    //                 Room management APIs
    //
    //////////////////////////////////////////////////////////

    /**
     * Create room (called by anchor)
     *
     * The normal calling process of the anchor is as follows:
     * 1. The anchor calls `createRoom` to create a voice chat room. At this time, room attribute information such as the room ID, whether mic-on needs confirmation by the anchor, and the number of seats is passed in.
     * 2. After successfully creating the room, the anchor calls `enterSeat` to enter the seat.
     * 3. The anchor receives the `onSeatListChange` seat list change event notification from the component. At this time, the seat list change can be refreshed and displayed on the UI.
     * 4. The anchor will also receive the `onAnchorEnterSeat` event notification of that a member entered the seat list. At this time, mic capturing will be automatically enabled.
     *
     * @param roomId Room ID. You need to assign and manage the IDs in a centralized manner.
     * @param roomParam Room description information, such as room name and cover information. If both the room list and room information are managed on your server, you can ignore this parameter.
     * @param callback Callback for room creation result. The `code` will be 0 if the operation succeeds.
     */
    public abstract void createRoom(int roomId, TRTCVoiceRoomDef.RoomParam roomParam, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Terminate room (called by anchor)
     *
     * After creating a room, the anchor can call this API to terminate it.
     */
    public abstract void destroyRoom(TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Enter room (called by viewer)
     *
     * Generally, the viewer can watch a live stream in the following call process:
     * 1. The **viewer** gets the latest voice chat room list from your server. The list may contain `roomId` and room information of multiple rooms.
     * 2. The viewer selects a voice chat room and calls `enterRoom` and passes in the room ID to enter the room.
     * 3. After room entry, the component's `onRoomInfoChange` room attribute change event notification will be received. At this time, the room attributes can be recorded, and corresponding changes can be made, such as the room name displayed on the UI and whether mic-on requires approval by the anchor.
     * 4. After room entry, the `onSeatListChange` seat list change event notification will be received from the component. At this time, the seat list change can be refreshed and displayed on the UI.
     * 5. After room entry, the `onAnchorEnterSeat` event notification that the anchor entered the seat list will also be received.
     *
     * @param roomId Room ID
     * @param callback callback Result callback for whether room entry succeeds
     */
    public abstract void enterRoom(int roomId, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Exit room
     *
     * @param callback callback Result callback for whether room exit succeeds
     */
    public abstract void exitRoom(TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Get room list details
     *
     * The details are set through `roomParam` by the anchor during `createRoom()`. If both the room list and room information are managed on your server, you can ignore this function.
     *
     * @param roomIdList Room ID list
     * @param callback Callback for room details
     */
    public abstract void getRoomInfoList(List<Integer> roomIdList, TRTCVoiceRoomCallback.RoomInfoCallback callback);

    /**
     * Get the user information of the specified `userId`. If the value is `null`, the information of all users in the room will be obtained
     *
     * @param userIdList User ID list
     * @param userlistcallback Callback for user details
     */
    public abstract void getUserInfoList(List<String> userIdList, TRTCVoiceRoomCallback.UserListCallback userlistcallback);

    //////////////////////////////////////////////////////////
    //
    //                 seat management APIs
    //
    //////////////////////////////////////////////////////////

    /**
     * Actively mic on (called by anchor or viewer)
     *
     * After successful mic-on, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorEnterSeat`.
     *
     * @param seatIndex Seat number for mic-on
     * @param callback Operation callback
     */
    public abstract void enterSeat(int seatIndex, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Actively mic off (called by anchor or viewer)
     *
     * After successful mic-off, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorLeaveSeat`.
     *
     * @param callback Operation callback
     */
    public abstract void leaveSeat(TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Pick a viewer for mic-on (called by anchor)
     *
     * After the anchor picks the viewer for mic-on, all members in the room will receive event notifications of `onSeatListChange` and `onAnchorEnterSeat`.
     *
     * - parameter seatIndex    Seat number for picked mic-on
     * - parameter userId       User ID
     * - parameter callback     Operation callback
     * @param seatIndex Seat number for picked mic-on
     * @param userId  User ID
     * @param callback Operation callback
     */
    public abstract void pickSeat(int seatIndex, String userId, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Kick off a viewer for mic-off (called by anchor)
     *
     * After the anchor kicks off the viewer for mic-off, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorLeaveSeat`.
     *
     * @param seatIndex Seat number for kicked mic-off
     * @param callback  Operation callback
     */
    public abstract void kickSeat(int seatIndex, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Mute/Unmute seat (called by anchor)
     *
     * @param seatIndex Seat number
     * @param isMute   true: muted; false: unmuted
     * @param callback Operation callback
     */
    public abstract void muteSeat(int seatIndex, boolean isMute, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Block/Unblock seat (called by anchor)
     *
     * @param seatIndex Seat number
     * @param isClose   true: blocked; false: unblocked
     * @param callback Operation callback
     */
    public abstract void closeSeat(int seatIndex, boolean isClose, TRTCVoiceRoomCallback.ActionCallback callback);

    //////////////////////////////////////////////////////////
    //
    //                 本地音频操作接口
    //
    //////////////////////////////////////////////////////////
    /**
     * Start mic capturing
     */
    public abstract void startMicrophone();

    /**
     * Stop mic capturing
     */
    public abstract void stopMicrophone();

    /**
     * Set sound quality
     *
     * @param quality TRTC_AUDIO_QUALITY_MUSIC/TRTC_AUDIO_QUALITY_DEFAULT/TRTC_AUDIO_QUALITY_SPEECH
     */
    public abstract void setAudioQuality(int quality);

    /**
     * Mute local audio
     * @param mute Whether to mute
     */
    public abstract void muteLocalAudio(boolean mute);

    /**
     * Enable speaker
     *
     * @param useSpeaker true: speaker; false: receiver
     */
    public abstract void setSpeaker(boolean useSpeaker);

    /**
     * Set mic capturing volume level
     *
     * @param volume Capturing volume level between 0 and 100
     */
    public abstract void setAudioCaptureVolume(int volume);

    /**
     * set playout volume level
     * @param volume playoutme volume level between 0 and 100
     */
    public abstract void setAudioPlayoutVolume(int volume);

    //////////////////////////////////////////////////////////
    //
    //                 remote user APIs
    //
    //////////////////////////////////////////////////////////
    /**
     * Mute the specified user's audio
     *
     * @param userId   User ID
     * @param mute true: muted; false: unmuted
     */
    public abstract void muteRemoteAudio(String userId, boolean mute);

    /**
     * Mute all users' audio
     *
     * @param mute true: muted; false: unmuted
     */
    public abstract void muteAllRemoteAudio(boolean mute);

    /**
     * Sound effect control APIs
     */
    public abstract TXAudioEffectManager getAudioEffectManager();

    //////////////////////////////////////////////////////////
    //
    //                message sending apis
    //
    //////////////////////////////////////////////////////////

    /**
     * Broadcast a text message in the room, which is generally used for on-screen comment chat
     *
     * - parameter message  Text message
     * - parameter callback Callback for sending result
     */
    public abstract void sendRoomTextMsg(String message, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Broadcast a custom (command) message in the room, which is generally used to broadcast liking and gifting messages
     *
     * - parameter cmd      Custom command word used to distinguish between different message types
     * - parameter message  Text message
     * - parameter callback Callback for sending result
     */
    public abstract void sendRoomCustomMsg(String cmd, String message, TRTCVoiceRoomCallback.ActionCallback callback);

    //////////////////////////////////////////////////////////
    //
    //                 invitation command message APIs
    //
    //////////////////////////////////////////////////////////
    /**
     * Send invitation to user
     *
     * - parameter cmd      Custom command of business
     * - parameter userId   Invitee user ID
     * - parameter content  Invitation content
     * - parameter callback Callback for sending result
     * - returns: inviteId Invitation ID
     */
    public abstract String sendInvitation(String cmd, String userId, String content, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Accept invitation
     *
     * - parameter identifier   Invitation ID
     * - parameter callback     Operation callback for invitation acceptance
     */
    public abstract void acceptInvitation(String id, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Decline invitation
     * - parameter identifier   Invitation ID
     * - parameter callback     Operation callback for invitation acceptance
     */
    public abstract void rejectInvitation(String id, TRTCVoiceRoomCallback.ActionCallback callback);

    /**
     * Cancel invitation
     * - parameter identifier   Invitation ID
     * - parameter callback     Operation callback for invitation acceptance
     */
    public abstract void cancelInvitation(String id, TRTCVoiceRoomCallback.ActionCallback callback);
}
