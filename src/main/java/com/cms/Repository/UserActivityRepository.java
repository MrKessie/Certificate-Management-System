package com.cms.Repository;

import com.cms.Model.User;
import com.cms.Model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
    List<UserActivity> findByUserOrderByTimestampDesc(User user);

    List<UserActivity> findAllByOrderByTimestampDesc();
}
