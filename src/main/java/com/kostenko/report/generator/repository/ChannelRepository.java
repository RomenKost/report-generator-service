package com.kostenko.report.generator.repository;

import com.kostenko.report.generator.entity.ChannelIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelIdEntity, String> {

}
