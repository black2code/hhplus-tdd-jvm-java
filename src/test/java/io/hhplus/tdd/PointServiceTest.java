package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.NotFoundException;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointService;
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

    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pointService = new PointService((userPointTable));
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

}
