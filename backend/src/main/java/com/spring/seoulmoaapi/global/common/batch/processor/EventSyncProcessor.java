package com.spring.seoulmoaapi.global.common.batch.processor;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.event.repository.EventRepository;
import com.spring.seoulmoaapi.global.common.batch.dto.EventSync;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventSyncProcessor implements ItemProcessor<EventSync, Event> {

    private final EventRepository eventRepository;

    @Override
    public Event process(EventSync sync) throws Exception {
        Optional<Event> optionalEvent = eventRepository.findByHomepage(sync.getHomepage());
        // 이미 존재 → UPDATE
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.updateFromSync(sync);
            return event;
        } else {
            // 존재하지 않음 → INSERT
            return Event.builder()
                    .title(sync.getTitle())
                    .categoryName(sync.getCategoryName())
                    .gu(sync.getGu())
                    .location(sync.getLocation())
                    .startDate(sync.getStartDate())
                    .endDate(sync.getEndDate())
                    .fee(sync.getFee())
                    .isFree(sync.getIsFree())
                    .latitude(sync.getLatitude())
                    .longitude(sync.getLongitude())
                    .homepage(sync.getHomepage())
                    .imageUrl(sync.getImageUrl())
                    .detailUrl(sync.getDetailUrl())
                    .targetUser(sync.getTargetUser())
                    .eventDescription(sync.getEventDescription())
                    .categoryId(null)
                    .build();
        }
    }
}