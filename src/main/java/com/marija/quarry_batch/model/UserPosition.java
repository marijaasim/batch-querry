package com.marija.quarry_batch.model;

import java.util.Objects;

public class UserPosition {

    private Long userId;
    private Long positionId;

    public UserPosition() {
    }

    public UserPosition(Long userId, Long positionId) {
        this.userId = userId;
        this.positionId = positionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPosition that = (UserPosition) o;
        return Objects.equals(userId, that.userId) && Objects.equals(positionId, that.positionId);
    }

    @Override
    public String toString() {
        return "UserPosition{" +
                "userId=" + userId +
                ", positionId=" + positionId +
                '}';
    }
}
