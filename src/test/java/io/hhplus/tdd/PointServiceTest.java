package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NotFoundException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PointServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("테스트 완료: " + testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test getUserPoint - Success Case")
    void testGetUserPoint() {
        // Given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 100, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        //When
        UserPoint result = pointService.getUserPoint(userId);

        //Then
        assertThat(result).isEqualTo(userPoint);
    }

    @Test
    @DisplayName("Test getUserPoint - Not Found Case")
    void testGetUserPointNotFound() {
        //Given
        long userId = -1L;
        when(userPointTable.selectById(userId)).thenReturn(null);

        //When & Then
        assertThrows(NotFoundException.class, () -> pointService.getUserPoint(userId));
    }

    @Test
    @DisplayName("Test testGetUserPointWithZeroId - Not Found Case")
    void testGetUserPointWithZeroId() {
        // Given
        long userId = 0L;
        when(userPointTable.selectById(userId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundException.class, () -> pointService.getUserPoint(userId));
    }

    @Test
    @DisplayName("Test getPointHistories - Success Case")
    void testGetPointHistories() {
        // Given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 100, System.currentTimeMillis());
        List<PointHistory> histories = List.of(
            new PointHistory(1L, userId, 50, TransactionType.CHARGE, System.currentTimeMillis()),
            new PointHistory(2L, userId, 30, TransactionType.USE, System.currentTimeMillis())
        );
        when(userPointTable.selectById(userId)).thenReturn(userPoint);
        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(histories);

        // When
        List<PointHistory> result = pointService.getPointHistories(userId);

        // Then
        assertThat(result).isEqualTo(histories);
    }

    @Test
    @DisplayName("Test getPointHistories - User Not Found Case")
    void testGetPointHistoriesUserNotFound() {
        // Given
        long userId = 1L;
        when(userPointTable.selectById(userId)).thenReturn(null);

        // When & Then
        assertThrows(NotFoundException.class, () -> pointService.getPointHistories(userId));
    }

}
