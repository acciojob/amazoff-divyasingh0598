
package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orders;
    private HashMap<String, DeliveryPartner> deliveryPartners;

    private HashMap<String, List<String>> partnerOrders;

    public OrderRepository(){
        this.orders = new HashMap<String, Order>();
        this.deliveryPartners = new HashMap<String, DeliveryPartner>();
        this.partnerOrders = new HashMap<String, List<String>>();
    }

    public void addOrder(Order order){
        orders.put(order.getId(),order);
    }


    public void addPartner(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartners.put(partnerId,deliveryPartner);
    }



    public void addOrderPartnerPair(String orderId,String partnerId){

        if(orders.containsKey(orderId) && deliveryPartners.containsKey(partnerId)){
            orders.put(orderId, orders.get(orderId));
            deliveryPartners.put(partnerId, deliveryPartners.get(partnerId));
            List<String> currentOrders = new ArrayList<String>();
            if(partnerOrders.containsKey(partnerId)) currentOrders = partnerOrders.get(partnerId);
            currentOrders.add(orderId);
            partnerOrders.put(partnerId, currentOrders);
        }
        DeliveryPartner d = deliveryPartners.get(partnerId);
        d.setNumberOfOrders(d.getNumberOfOrders()+1);
    }



    public Order getOrderById(String orderId){
        return orders.get(orderId);
    }



    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartners.get(partnerId);
    }


    public int getOrderCountByPartnerId(String partnerId){
        List<String> orderCount = new ArrayList<String>();
        if(partnerOrders.containsKey(partnerId)) orderCount = partnerOrders.get(partnerId);
        return orderCount.size();
    }


    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> oil = new ArrayList<String>();
        if(partnerOrders.containsKey(partnerId)) oil = partnerOrders.get(partnerId);
        return oil;
    }



    public List<String> getAllOrders(){
        List<String> allOrders = new ArrayList<String>();
        orders.forEach((k, v)-> allOrders.add(k));
        return allOrders;
    }



    public int getCountOfUnassignedOrders(){
        List<String> allOrder = new ArrayList<String>();
        List<String> assOrder;
        orders.forEach((k, v)-> allOrder.add(k));
        for (Map.Entry<String,List<String>> mapElement : partnerOrders.entrySet()) {
            assOrder = new ArrayList<String>();
            assOrder = mapElement.getValue();
            for(String s : assOrder){
                allOrder.remove(s);
            }
        }
        return allOrder.size();
    }


    public void deletePartnerById(String partnerId){
        if(partnerOrders.containsKey(partnerId)){
            partnerOrders.remove(partnerId);
        }
        if(deliveryPartners.containsKey(partnerId)){
            deliveryPartners.remove(partnerId);
        }
    }


    public void deleteOrderById(String orderId) {
        List<String> assOrder;
        for (Map.Entry<String, List<String>> mapElement : partnerOrders.entrySet()) {
            assOrder = new ArrayList<String>();
            assOrder = mapElement.getValue();
            for (String s : assOrder) {
                if(s.equals(orderId)){
                    DeliveryPartner d = deliveryPartners.get(mapElement.getKey());
                    d.setNumberOfOrders(d.getNumberOfOrders()-1);
                    assOrder.remove(s);
                    partnerOrders.put(mapElement.getKey(),assOrder);
                    break;
                }
            }
        }
        if(orders.containsKey(orderId)){
            orders.remove(orderId);
        }
    }
}
