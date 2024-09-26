package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NotFoundException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint getUserPoint(long userId) {
        return Optional.ofNullable(userPointTable.selectById(userId))
            .orElseThrow(() -> new NotFoundException("User not found userid : " + userId));
    }

    public List<PointHistory> getPointHistories(long userId) {
        if (userPointTable.selectById(userId) == null) {
            throw new NotFoundException("User not found userid: " + userId);
        }
        return pointHistoryTable.selectAllByUserId(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }

        UserPoint userPoint = Optional.ofNullable(userPointTable.selectById(userId))
            .orElseThrow(() -> new NotFoundException("User not found userid: " + userId));

        long updatedPoint = userPoint.point() + amount;
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, updatedPoint);

        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return updatedUserPoint;
    }

    public UserPoint usePoint(long userId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }

        UserPoint userPoint = Optional.ofNullable(userPointTable.selectById(userId))
            .orElseThrow(() -> new NotFoundException("User not found userid: " + userId));

        if (userPoint.point() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        long updatedPoint = userPoint.point() - amount;
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, updatedPoint);

        pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());

        return updatedUserPoint;
    }

}
