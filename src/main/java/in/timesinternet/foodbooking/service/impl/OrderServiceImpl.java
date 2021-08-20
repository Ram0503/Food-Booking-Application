package in.timesinternet.foodbooking.service.impl;

import in.timesinternet.foodbooking.dto.request.ApplyCouponResponseDto;
import in.timesinternet.foodbooking.dto.request.OrderDto;
import in.timesinternet.foodbooking.entity.Customer;
import in.timesinternet.foodbooking.entity.Order;
import in.timesinternet.foodbooking.entity.Payment;
import in.timesinternet.foodbooking.entity.enumeration.CartStatus;
import in.timesinternet.foodbooking.entity.enumeration.OrderStatus;
import in.timesinternet.foodbooking.entity.enumeration.PaymentMode;
import in.timesinternet.foodbooking.entity.enumeration.PaymentStatus;
import in.timesinternet.foodbooking.repository.OrderRepository;
import in.timesinternet.foodbooking.service.CartService;
import in.timesinternet.foodbooking.service.CouponService;
import in.timesinternet.foodbooking.service.CustomerService;
import in.timesinternet.foodbooking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    CouponService couponService;

    @Autowired
    CartService cartService;

    @Override
    public Order createOrder(OrderDto orderDto, String userEmail) {
        Customer customer = customerService.getCustomer(userEmail);
        Order order = new Order();
        if (orderDto.getCouponName() != null) {
            ApplyCouponResponseDto applyCouponResponseDto = cartService.addCouponOnCurrentCart(orderDto.getCouponName(), userEmail);
            order.setCoupon(couponService.getCoupon(applyCouponResponseDto.getCouponId()));
            order.setIsCouponApplied(true);
            order.setDiscount(applyCouponResponseDto.getDiscountedValue());
        }

        order.setCustomer(customer);
        order.setAddress(orderDto.getAddress());
        order.setContact(orderDto.getContact());
        order.setType(order.getType());
        order.setTotal(customer.getCurrentCart().getTotal()-order.getDiscount());
        order.setType(orderDto.getOrderType());
        order.setRestaurant(customer.getRestaurant());
        order.setCart(customer.getCurrentCart());


        //create and associate payment
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMode(PaymentMode.COD);

        order.setPayment(payment);
        try {
            validateOrder();
            order.setStatus(OrderStatus.PENDING);
            cartService.addNewCart(userEmail);

        } catch (RuntimeException exception) {
            cartService.updateCartStatus(CartStatus.MUTABLE, userEmail);
            order.setStatus(OrderStatus.DECLINE);
        }
        order = orderRepository.save(order);
        return order;
    }

    private void validateOrder() throws RuntimeException {
        //throw exception is invalid order request
    }

}
