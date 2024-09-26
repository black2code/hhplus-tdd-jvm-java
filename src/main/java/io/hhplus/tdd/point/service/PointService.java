package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NotFoundException;
import io.hhplus.tdd.point.PointHistory;
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
}
