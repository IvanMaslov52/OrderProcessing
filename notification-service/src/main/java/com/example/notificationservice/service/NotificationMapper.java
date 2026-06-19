package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.model.OrderNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto notificationToDto(OrderNotification notification);

    NotificationResponse notificationToResponse(OrderNotification notification);
}
