package com.cms.Service;

import com.cms.Model.User;
import com.cms.Model.UserActivity;
import com.cms.Repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    public void logActivity(User user, String action, String details) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setAction(action);
        activity.setDetails(details);
        activity.setTimestamp(LocalDateTime.now());
        userActivityRepository.save(activity);
    }

//    public List<UserActivity> getUserActivities(User user) {
//        return userActivityRepository.findByUserOrderByTimestampDesc(user);
//    }

    public List<UserActivity> getAllUserActivities() {
        return userActivityRepository.findAllByOrderByTimestampDesc();
    }
}
