package com.imkarl.call.audio;

/**
 * 通话事件
 * @author imkarl 2016-09
 */
public class CallEvent {

    public enum Direction {
        /** 来电 */
        INCOMING,
        /** 去电(拨号) */
        DIAL;
    }

    private String callId;
    private String userId;
    private Direction direction;
    private EventType type;
    private int code;

    public CallEvent() {
    }
    public CallEvent(String callId, String userId, Direction direction, EventType type, int code) {
        this.callId = callId;
        this.userId = userId;
        this.direction = direction;
        this.type = type;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CallEvent{" +
                "callId='" + callId + '\'' +
                ", userId='" + userId + '\'' +
                ", direction=" + direction +
                ", type=" + type +
                ", code=" + code +
                '}';
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCallId() {
        return callId;
    }

    public String getUserId() {
        return userId;
    }

    public Direction getDirection() {
        return direction;
    }

    public EventType getType() {
        return type;
    }

    public int getCode() {
        return code;
    }
}
