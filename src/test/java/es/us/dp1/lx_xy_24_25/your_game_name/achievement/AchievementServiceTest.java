package es.us.dp1.lx_xy_24_25.your_game_name.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lx_xy_24_25.your_game_name.statistics.achievement.Achievement;
import es.us.dp1.lx_xy_24_25.your_game_name.statistics.achievement.AchievementService;

@SpringBootTest
public class AchievementServiceTest {
    @Autowired
	private AchievementService achievementService;

    @Test
	void shouldFindAllAchievements() {
		List<Achievement> achievement = (List<Achievement>) this.achievementService.getAchievements();
		assertEquals(4, achievement.size());
	}

    @Test
    void shouldNotFindAchievementByName(){
        Achievement achievement = this.achievementService.getAchievementByName("Nonexistent Achievement");
        assertEquals(null, achievement);
    }
    @Test
    void shouldFindAchievementById() {
        Achievement achievement = this.achievementService.getById(1);
        assertEquals(1, achievement.getId());
    }

    @Test 
    void shouldNotFindAchievementById() {
        Achievement achievement = this.achievementService.getById(999);
        assertEquals(null, achievement);
    }   

    @Test
    void shouldFindAchievementByName() {
        Achievement achievement = this.achievementService.getAchievementByName("Pescador Experto");
        assertEquals("Pescador Experto", achievement.getName());
    }

    @Test
    @Transactional
   	void shouldInsertAchievement() {
		int count = ((Collection<Achievement>) this.achievementService.getAchievements()).size();

		Achievement achievement = new Achievement();
		achievement.setName("Prueba de Achivement");
		achievement.setThreshold(5);
		achievement.setBadgeImage("http://example.com/badge.png");
        achievement.setDescription("Descripción de prueba");
        achievement.setMetric(es.us.dp1.lx_xy_24_25.your_game_name.statistics.Metric.VICTORIES);

		this.achievementService.saveAchievement(achievement);
		assertNotEquals(0, achievement.getId().longValue());
		assertNotNull(achievement.getId());

		int finalCount = ((Collection<Achievement>) this.achievementService.getAchievements()).size();
		assertEquals(count + 1, finalCount);
	}


    @Test
    @Transactional
    void shouldDeleteAchievementById() {
        int count = ((Collection<Achievement>) this.achievementService.getAchievements()).size();

		Achievement achievement = new Achievement();
		achievement.setName("Prueba de Achivement");
		achievement.setThreshold(5);
		achievement.setBadgeImage("http://example.com/badge.png");
        achievement.setDescription("Descripción de prueba");
        achievement.setMetric(es.us.dp1.lx_xy_24_25.your_game_name.statistics.Metric.VICTORIES);

		this.achievementService.saveAchievement(achievement);
        int secCount = ((Collection<Achievement>) this.achievementService.getAchievements()).size();
        assertEquals(count+1, secCount);
        achievementService.deleteAchievementById(achievement.getId());
		
        int countAfterDelete= ((Collection<Achievement>) this.achievementService.getAchievements()).size();
        assertEquals(count, countAfterDelete);    
    }




}
