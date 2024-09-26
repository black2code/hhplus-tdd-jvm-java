package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NotFoundException;
import io.hhplus.tdd.point.UserPoint;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint getUserPoint(long userId) {
        return Optional.ofNullable(userPointTable.selectById(userId))
            .orElseThrow(() -> new NotFoundException("User not found userid : " + userId));
    }
}
