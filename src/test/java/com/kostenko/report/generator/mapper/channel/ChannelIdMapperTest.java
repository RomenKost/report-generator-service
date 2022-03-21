package com.kostenko.report.generator.mapper.channel;

import com.kostenko.report.generator.entity.ChannelIdEntity;
import com.kostenko.report.generator.entity.Entities;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.dto.channel.ChannelIdDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = ChannelIdMapperTest.class)
@ComponentScan(basePackageClasses = ChannelIdMapper.class)
class ChannelIdMapperTest {
    @Autowired
    private ChannelIdMapper channelIdMapper;

    @Test
    void channelIdToChannelIdEntityTest() {
        ChannelId channelId = Models.getChannelId();
        ChannelIdEntity expected = Entities.getChannelId();

        ChannelIdEntity actual = channelIdMapper.channelIdToChannelIdEntity(channelId);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdToChannelIdEntityWhenChannelIdIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdToChannelIdEntity(null));
    }

    @Test
    void channelIdEntityToChannelIdTest() {
        ChannelIdEntity channelIdEntity = Entities.getChannelId();
        ChannelId expected = Models.getChannelId();

        ChannelId actual = channelIdMapper.channelIdEntityToChannelId(channelIdEntity);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdEntityToChannelIdWhenChannelIdEntityIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdEntityToChannelId(null));
    }

    @Test
    void channelIdToChannelIdDtoTest() {
        ChannelId channelId = Models.getChannelId();
        ChannelIdDto expected = DTOs.getChannelId();

        ChannelIdDto actual = channelIdMapper.channelIdToChannelIdDTO(channelId);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdToChannelIdDtoWhenChannelIdIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdToChannelIdDTO(null));
    }

    @Test
    void channelIdDtoToChannelIdTest() {
        ChannelIdDto channelIdDto = DTOs.getChannelId();
        ChannelId expected = Models.getChannelId();

        ChannelId actual = channelIdMapper.channelIdDtoToChannelId(channelIdDto);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdDtoToChannelIdWhenChannelIdDtoIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdDtoToChannelId(null));
    }

    @Test
    void channelIdsToChannelIdEntitiesTest() {
        List<ChannelId> channelIds = Models.getChannelIds();
        List<ChannelIdEntity> expected = Entities.getChannelIds();

        List<ChannelIdEntity> actual = channelIdMapper.channelIdsToChannelIdEntities(channelIds);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdsToChannelIdEntitiesWhenChannelIdsIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdsToChannelIdEntities(null));
    }

    @Test
    void channelIdEntitiesToChannelIdsTest() {
        List<ChannelIdEntity> channelIdEntity = Entities.getChannelIds();
        List<ChannelId> expected = Models.getChannelIds();

        List<ChannelId> actual = channelIdMapper.channelIdEntitiesToChannelIds(channelIdEntity);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdEntitiesToChannelIdsWhenChannelIdEntitiesIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdEntitiesToChannelIds(null));
    }

    @Test
    void channelIdsToChannelIdDTOsTest() {
        List<ChannelId> channelIds = Models.getChannelIds();
        List<ChannelIdDto> expected = DTOs.getChannelIds();

        List<ChannelIdDto> actual = channelIdMapper.channelIdsToChannelIdDTOs(channelIds);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdsToChannelIdDTOsWhenChannelIdsIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdsToChannelIdDTOs(null));
    }


    @Test
    void channelIdDTOsToChannelIdsTest() {
        List<ChannelIdDto> channelIdDto = DTOs.getChannelIds();
        List<ChannelId> expected = Models.getChannelIds();

        List<ChannelId> actual = channelIdMapper.channelIdDTOsToChannelIds(channelIdDto);

        assertEquals(expected, actual);
    }

    @Test
    void channelIdDTOsToChannelIdsWhenChannelIdDTOsIsNullReturnNullTest() {
        assertNull(channelIdMapper.channelIdDtoToChannelId(null));
    }
}