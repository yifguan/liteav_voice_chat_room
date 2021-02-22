/**
 * Module: TCLog
 *
 * Function: 日志模块
 */

#import <Foundation/Foundation.h>


@interface TCLog : NSObject

+ (instancetype)shareInstance;

- (void)log:(NSString *)formatStr, ...;

- (void)onLog:(NSString*)log LogLevel:(int)level WhichModule:(NSString *)module;

@end
