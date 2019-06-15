package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.DashboardRepository;

@Transactional
@Service
public class DashboardService {
	
	// Managed repository ------------------------------
	
	@Autowired
	private DashboardRepository dashboardRepository;
	
	// Supporting services -----------------------
	
	public Double[] StatsSubmissionsPerConference() {
		return this.dashboardRepository.statsSubmissionsPerConference();
	}
	
	public Double[] StatsRegistrationsPerConference() {
		return this.dashboardRepository.statsRegistrationsPerConference();
	}
	
	public Double[] StatsConferenceFees() {
		return this.dashboardRepository.statsConferenceFees();
	}
	
	public Double[] StatsDaysPerConference() {
		return this.dashboardRepository.statsDaysPerConference();
	}
	
	public Double[] StatsConferencesPerCategory() {
		return this.dashboardRepository.statsConferencesPerCategory();
	}
	
	public Double[] StatsCommentsPerConference() {
		return this.dashboardRepository.statsCommentsPerConference();
	}
	
	public Double[] StatsCommentsPerActivity() {
		return this.dashboardRepository.statsCommentsPerActivity();
	}

}
