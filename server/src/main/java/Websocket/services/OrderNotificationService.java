package Websocket.services;

import Orders.DTOs.OrderCustomerDTO;
import Orders.domain.Order;
import Orders.mappers.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class OrderNotificationService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private OrderMapper orderMapper;

    //NOTIFIES KITCHEN COOKS ABOUT THE ORDERS
    public void notifyOrderToKitchen(Order order){
        OrderCustomerDTO orderCustomerDTO= orderMapper.toCustomerOrderDTO(order);
        simpMessagingTemplate.convertAndSend("/topic/kitchen", orderCustomerDTO);
    }

    public void notifyOrderStatusToKitchen(Order order){

    }
}
