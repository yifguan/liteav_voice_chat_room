//
//  TRTCVoiceRoom.h
//  TRTCVoiceRoomOCDemo
//
//  Created by abyyxwang on 2020/6/30.
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TRTCVoiceRoomDelegate.h"
#import "TRTCVoiceRoomDef.h"

NS_ASSUME_NONNULL_BEGIN

@class TXAudioEffectManager;
@interface TRTCVoiceRoom : NSObject

/**
* Get the `TRTCVoiceRoom` singleton object
*
* - returns: `TRTCVoiceRoom` instance
* - note: `{@link TRTCVoiceRoom#destroySharedInstance()}` can be called to terminate a singleton object
*/
+ (instancetype)sharedInstance NS_SWIFT_NAME(shared());

/**
* Terminate the `TRTCVoiceRoom` singleton object
*
* - note: after the instance is terminated, the externally cached `TRTCVoiceRoom` instance cannot be reused, and you need to call `{@link TRTCVoiceRoom#sharedInstance()}` again to get a new instance
*/
+ (void)destroySharedInstance NS_SWIFT_NAME(destroyShared());

#pragma mark: - basic APIs
/**
* Set the component callback
* <p>
* You can use `TRTCVoiceRoomDelegate` to get various status notifications of `TRTCVoiceRoom`
*
* - parameter delegate Callback API
* - note: callback events in `TRTCVoiceRoom` are called back to you in the main queue by default. If you need to specify a queue for event callback, please use `{@link TRTCVoiceRoom#setDelegateQueue(queue)}`
*/
- (void)setDelegate:(id<TRTCVoiceRoomDelegate>)delegate NS_SWIFT_NAME(setDelegate(delegate:));

/**
* Set the queue where the event callback is
*
* - parameter queue Queue. Various status callback notifications in `TRTCVoiceRoom` will be sent to the queue you specify.
*/
- (void)setDelegateQueue:(dispatch_queue_t)queue NS_SWIFT_NAME(setDelegateQueue(queue:));

/**
* Log in
*
* - parameter sdkAppID You can view the `SDKAppID` in the TRTC console > **[Application Management](https://console.cloud.tencent.com/trtc/app)** > "Application Information"
* - parameter userId   ID of the current user, which is a string that can contain only letters (a–z and A–Z), digits (0–9), hyphens (-), and underscores (_)
* - parameter userSig  Tencent Cloud's proprietary security protection signature. For more information on how to get it, please see [UserSig](https://cloud.tencent.com/document/product/647/17275).
* - parameter callback Login callback. The `code` will be 0 if login is successful
*/
- (void)login:(int)sdkAppID
       userId:(NSString *)userId
      userSig:(NSString *)userSig
     callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(login(sdkAppID:userId:userSig:callback:));

/**
* Log off
*/
- (void)logout:(ActionCallback _Nullable)callback NS_SWIFT_NAME(logout(callback:));


/**
* Set user information. The user information you set will be stored in Tencent Cloud IM.
*
* - parameter userName     User nickname, which cannot be nil
* - parameter avatarURL    User profile photo
* - parameter callback     Result callback for whether the setting succeeds
*/
- (void)setSelfProfile:(NSString *)userName avatarURL:(NSString *)avatarURL callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(setSelfProfile(userName:avatarURL:callback:));

#pragma mark - room management APIs
/**
* Create room (called by anchor)
*
* The normal calling process of the anchor is as follows:
* 1. The anchor calls `createRoom` to create a voice chat room. At this time, room attribute information such as the room ID, whether mic-on needs confirmation by the anchor, and the number of seats is passed in.
* 2. After successfully creating the room, the anchor calls `enterSeat` to enter the seat.
* 3. The anchor receives the `onSeatListChange` seat list change event notification from the component. At this time, the seat list change can be refreshed and displayed on the UI.
* 4. The anchor will also receive the `onAnchorEnterSeat` event notification of that a member entered the seat list. At this time, mic capturing will be automatically enabled.
*
* - parameter roomID       Room ID. You need to assign and manage the IDs in a centralized manner.
* - parameter roomParam    Room description information, such as room name and cover information. If both the room list and room information are managed on your server, you can ignore this parameter.
* - parameter callback     Callback for room creation result. The `code` will be 0 if the operation succeeds.
*/
- (void)createRoom:(int)roomID roomParam:(VoiceRoomParam *)roomParam callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(createRoom(roomID:roomParam:callback:));

/**
* Terminate room (called by anchor)
*
* After creating a room, the anchor can call this API to terminate it.
*/
- (void)destroyRoom:(ActionCallback _Nullable)callback NS_SWIFT_NAME(destroyRoom(callback:));

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
* - parameter roomID   Room ID
* - parameter callback Result callback for whether room entry succeeds
*/
- (void)enterRoom:(NSInteger)roomID callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(enterRoom(roomID:callback:));

/**
* Exit room
*
* - parameter callback Result callback for whether room exit succeeds
*/
- (void)exitRoom:(ActionCallback _Nullable)callback NS_SWIFT_NAME(exitRoom(callback:));

/**
* Get room list details
*
* The details are set through `roomParam` by the anchor during `createRoom()`. If both the room list and room information are managed on your server, you can ignore this function.
*
* - parameter roomIdList   Room ID list
* - parameter callback     Callback for room details
*/
- (void)getRoomInfoList:(NSArray<NSNumber *> *)roomIdList callback:(VoiceRoomInfoCallback _Nullable)callback NS_SWIFT_NAME(getRoomInfoList(roomIdList:callback:));

/**
* Get the user information of the specified `userId`. If the value is `null`, the information of all users in the room will be obtained
*
* - parameter userIDList   User ID list
* - parameter callback     Callback for user details
*/
- (void)getUserInfoList:(NSArray<NSString *> * _Nullable)userIDList callback:(VoiceRoomUserListCallback _Nullable)callback NS_SWIFT_NAME(getUserInfoList(userIDList:callback:));

#pragma mark - seat management APIs
/**
* Actively mic on (called by anchor or viewer)
*
* After successful mic-on, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorEnterSeat`.
*
* - parameter seatIndex    Seat number for mic-on
* - parameter callback     Operation callback
*/
- (void)enterSeat:(NSInteger)seatIndex callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(enterSeat(seatIndex:callback:));

/**
* Actively mic off (called by anchor or viewer)
*
* After successful mic-off, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorLeaveSeat`.
*
* - parameter callback Operation callback
*/
- (void)leaveSeat:(ActionCallback _Nullable)callback NS_SWIFT_NAME(leaveSeat(callback:));

/**
* Pick a viewer for mic-on (called by anchor)
*
* After the anchor picks the viewer for mic-on, all members in the room will receive event notifications of `onSeatListChange` and `onAnchorEnterSeat`.
*
* - parameter seatIndex    Seat number for picked mic-on
* - parameter userId       User ID
* - parameter callback     Operation callback
*/
- (void)pickSeat:(NSInteger)seatIndex userId:(NSString *)userId callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(pickSeat(seatIndex:userId:callback:));

/**
 * Kick off a viewer for mic-off (called by anchor)
 *
 * After the anchor kicks off the viewer for mic-off, all members in the room will receive the event notifications of `onSeatListChange` and `onAnchorLeaveSeat`.
 *
 * - parameter seatIndex    Seat number for kicked mic-off
 * - parameter callback     Operation callback
 */
- (void)kickSeat:(NSInteger)seatIndex callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(kickSeat(seatIndex:callback:));

/**
* Mute/Unmute seat (called by anchor)
*
* - parameter seatIndex    Seat number
* - parameter isMute       true: muted; false: unmuted
* - parameter callback     Operation callback
*/
- (void)muteSeat:(NSInteger)seatIndex isMute:(BOOL)isMute callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(muteSeat(seatIndex:isMute:callback:));

/**
* Block/Unblock seat (called by anchor)
*
* - parameter seatIndex    Seat number
* - parameter isClose      true: blocked; false: unblocked
* - parameter callback     Operation callback
*/
- (void)closeSeat:(NSInteger)seatIndex isClose:(BOOL)isClose callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(closeSeat(seatIndex:isClose:callback:));

#pragma mark - local audio operation APIs

/**
* Start mic capturing
*/
- (void)startMicrophone;

/**
* Stop mic capturing
*/
- (void)stopMicrophone;

/**
* Set sound quality
*
* - parameter quality TRTC_AUDIO_QUALITY_MUSIC/TRTC_AUDIO_QUALITY_DEFAULT/TRTC_AUDIO_QUALITY_SPEECH
*/
- (void)setAuidoQuality:(NSInteger)quality NS_SWIFT_NAME(setAuidoQuality(quality:));

/**
* Mute local audio
*
* - parameter mute Whether to mute
*/
- (void)muteLocalAudio:(BOOL)mute NS_SWIFT_NAME(muteLocalAudio(mute:));

/**
* Enable speaker
*
* - parameter useSpeaker  true: speaker; false: receiver
*/
- (void)setSpeaker:(BOOL)userSpeaker NS_SWIFT_NAME(setSpeaker(userSpeaker:));

/**
* Set mic capturing volume level
*
* - parameter volume Capturing volume level between 0 and 100
*/
- (void)setAudioCaptureVolume:(NSInteger)voluem NS_SWIFT_NAME(setAudioCaptureVolume(volume:));

/**
 * set playout volume level
 * @param volume playoutme volume level between 0 and 100
 */
- (void)setAudioPlayoutVolume:(NSInteger)volume NS_SWIFT_NAME(setAudioPlayoutVolume(volume:));

#pragma mark - remote user APIs
/**
* Mute the specified user's audio
*
* - parameter userId   User ID
* - parameter mute     true: muted; false: unmuted
*/
- (void)muteRemoteAudio:(NSString *)userId mute:(BOOL)mute NS_SWIFT_NAME(muteRemoteAudio(userId:mute:));

/**
* Mute all users' audio
*
* - parameter isMute true: muted; false: unmuted
*/
- (void)muteAllRemoteAudio:(BOOL)isMute NS_SWIFT_NAME(muteAllRemoteAudio(isMute:));

/**
* Sound effect control APIs
*/
- (TXAudioEffectManager * _Nullable)getAudioEffectManager;

#pragma mark - message sending APIs
/**
* Broadcast a text message in the room, which is generally used for on-screen comment chat
*
* - parameter message  Text message
* - parameter callback Callback for sending result
*/
- (void)sendRoomTextMsg:(NSString *)message callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(sendRoomTextMsg(message:callback:));

/**
* Broadcast a custom (command) message in the room, which is generally used to broadcast liking and gifting messages
*
* - parameter cmd      Custom command word used to distinguish between different message types
* - parameter message  Text message
* - parameter callback Callback for sending result
*/
- (void)sendRoomCustomMsg:(NSString *)cmd message:(NSString *)message callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(sendRoomCustomMsg(cmd:message:callback:));

#pragma mark - invitation command message APIs

/**
* Send invitation to user
*
* - parameter cmd      Custom command of business
* - parameter userId   Invitee user ID
* - parameter content  Invitation content
* - parameter callback Callback for sending result
* - returns: inviteId Invitation ID
*/
- (NSString *)sendInvitation:(NSString *)cmd
                      userId:(NSString *)userId
                     content:(NSString *)content
                    callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(sendInvitation(cmd:userId:content:callback:));

/**
* Accept invitation
*
* - parameter identifier   Invitation ID
* - parameter callback     Operation callback for invitation acceptance
*/
- (void)acceptInvitation:(NSString *)identifier callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(acceptInvitation(identifier:callback:));


/**
* Decline invitation
* - parameter identifier   Invitation ID
* - parameter callback     Operation callback for invitation acceptance
*/
- (void)rejectInvitation:(NSString *)identifier callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(rejectInvitation(identifier:callback:));

/**
* Cancel invitation
* - parameter identifier   Invitation ID
* - parameter callback     Operation callback for invitation acceptance
*/
- (void)cancelInvitation:(NSString *)identifier callback:(ActionCallback _Nullable)callback NS_SWIFT_NAME(cancelInvitation(identifier:callback:));

@end

NS_ASSUME_NONNULL_END
