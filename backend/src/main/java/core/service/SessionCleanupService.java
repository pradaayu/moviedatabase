package core.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import core.model.UserLogin;
import core.repository.UserLoginRepository;

import java.util.List;

@Service
public class SessionCleanupService {
    
    private final UserLoginRepository userLoginRepository;

    private final int idleTimeout = 5 * 60 * 1000; // ms

    public SessionCleanupService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000) // Run every 5 minutes
    public void cleanUpIdleSessions() {
        long cutoff = System.currentTimeMillis() - idleTimeout;
        List<UserLogin> staleLogins = userLoginRepository.findAll()
            .stream()
            .filter(login -> login.getLastUseTime() < cutoff)
            .toList();
        staleLogins.forEach(login -> userLoginRepository.delete(login));
    }
}
