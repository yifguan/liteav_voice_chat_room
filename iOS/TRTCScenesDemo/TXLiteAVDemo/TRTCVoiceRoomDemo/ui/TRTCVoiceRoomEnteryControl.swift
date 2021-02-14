//
//  TRTCVoiceRoomEnteryControl.swift
//  TRTCVoiceRoomDemo
//
//  Created by abyyxwang on 2020/6/3.
//  Copyright © 2020 Tencent. All rights reserved.
//

import UIKit

/// ViewModel can be regarded as the Controller layer in the MVC architecture
/// It is responsible for the dependency injection of the voice chat room controller and ViewModel and the transfer of common parameters
/// ViewModel, ViewController
/// Note: this class is responsible for generating ViewController and ViewModel of all UI layers. Carefully hold the member variables of the UI layers; otherwise, circular references are prone to occur. Please be cautious when holding member variables!
class TRTCVoiceRoomEnteryControl: NSObject {
    // Read-only parameter, which is called externally during initialization.
    // `SDKAPPID` and `USERID` can also be global parameters, which can be flexibly adjusted according to your needs
    // The purpose of injecting this parameter is to decouple the VoiceRoomUI layer and the Login module
    private(set) var mSDKAppID: Int32 = 0
    private(set) var userId: String = ""
    
    /// Initialization method
    /// - Parameters:
    ///   - sdkAppId: inject the current `SDKAPPID`
    ///   - userId: inject the `userID`
    @objc convenience init(sdkAppId:Int32, userId: String) {
        self.init()
        self.mSDKAppID = sdkAppId
        self.userId = userId
    }
    
    deinit {
        TRTCLog.out("deinit \(type(of: self))")
    }
    /*
     `TRTCVoice` is a terminatable singleton.
     In the demo, a singleton object can be obtained or generated through `shardInstance` (OC) or `shared` (Swift)
     After terminating the singleton object, you need to call the `sharedInstance` API again to generate the instance again.
     This method is called in `VoiceRoomListRoomViewModel`, `CreateVoiceRoomViewModel`, and `VoiceRoomViewModel`
     Since it is a terminatable singleton, the purpose of generating and placing the object here is to manage the singleton generation path in a unified way and facilitate maintenance
     */
    private var voiceRoom: TRTCVoiceRoom?
    /// Get `VoiceRoom`
    /// - Returns: `VoiceRoom` singleton
    func getVoiceRoom() -> TRTCVoiceRoom {
        if let room = voiceRoom {
            return room
        }
        voiceRoom = TRTCVoiceRoom.shared()
        return voiceRoom!
    }
    /*
     When `VoiceRoom` is no longer needed, the singleton object can be terminated.
     For example, when you log off.
     This termination method is not called in this demo.
    */
    /// Terminate `voiceRoom` singleton
    func clearVoiceRoom() {
        TRTCVoiceRoom.destroyShared()
        voiceRoom = nil
    }
    
    
    /// Controller of the voice chat room entry
    /// - Returns: main entry to the voice chat room
    @objc func makeEntranceViewController() -> UIViewController {
       return makeVoiceRoomListViewController()
    }
    
    
    /// Create a voice chat room page
    /// - Returns: VC of the created voice chat room
    func makeCreateVoiceRoomViewController() -> UIViewController {
         return TRTCCreateVoiceRoomViewController.init(dependencyContainer: self)
    }
    
    
    /// Room list page
    /// - Returns: VC of the voice chat room list
    func makeVoiceRoomListViewController() -> UIViewController {
        return TRTCVoiceRoomListViewController.init(dependencyContainer: self)
    }
    
    /// Voice chat room
    /// - Parameters:
    ///   - roomInfo: parameter of the room to be created or entered
    ///   - role: role (viewer or anchor)
    /// - Returns: voice chat room controller
    func makeVoiceRoomViewController(roomInfo: VoiceRoomInfo, role: VoiceRoomViewType, toneQuality:VoiceRoomToneQuality = .music) -> UIViewController {
        return TRTCVoiceRoomViewController.init(viewModelFactory: self, roomInfo: roomInfo, role: role, toneQuality: toneQuality)
    }
}

extension TRTCVoiceRoomEnteryControl: TRTCVoiceRoomViewModelFactory {
    
    /// Create the logic layer of the voice chat room view (C in MVC or ViewModel in MVVM)
    /// - Returns: ViewModel of the created voice chat room page
    func makeCreateVoiceRoomViewModel() -> TRTCCreateVoiceRoomViewModel {
        return TRTCCreateVoiceRoomViewModel.init(container: self)
    }
    
    /// Logic layer of the voice chat room view (C in MVC or ViewModel in MVVM)
    /// - Parameters:
    ///   - roomInfo: voice chat room information
    ///   - roomType: role
    /// - Returns: ViewModel of the voice chat room page
    func makeVoiceRoomViewModel(roomInfo: VoiceRoomInfo, roomType: VoiceRoomViewType) -> TRTCVoiceRoomViewModel {
        return TRTCVoiceRoomViewModel.init(container: self, roomInfo: roomInfo, roomType: roomType)
    }
    
    /// Logic layer of the voice chat room list view (C in MVC or ViewModel in MVVM)
    /// - Returns: ViewModel of the voice chat room list
    func makeVoiceRoomListViewModel() -> TRTCVoiceRoomListViewModel {
        return TRTCVoiceRoomListViewModel.init(container: self)
    }
}
