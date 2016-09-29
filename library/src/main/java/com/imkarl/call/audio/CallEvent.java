package com.imkarl.call.audio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通话事件
 * @author imkarl 2016-09
 */
public class CallEvent implements Parcelable {

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.callId);
        dest.writeString(this.userId);
        dest.writeInt(this.direction == null ? -1 : this.direction.ordinal());
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.code);
    }

    protected CallEvent(Parcel in) {
        this.callId = in.readString();
        this.userId = in.readString();
        int tmpDirection = in.readInt();
        this.direction = tmpDirection == -1 ? null : Direction.values()[tmpDirection];
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : EventType.values()[tmpType];
        this.code = in.readInt();
    }

    public static final Creator<CallEvent> CREATOR = new Creator<CallEvent>() {
        @Override
        public CallEvent createFromParcel(Parcel source) {
            return new CallEvent(source);
        }

        @Override
        public CallEvent[] newArray(int size) {
            return new CallEvent[size];
        }
    };


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
