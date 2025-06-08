package onlineretailsystem;
import java.util.ArrayList;
import java.util.List;

// Observer interface
interface OrderStatusObserver {
    void update(String orderStatus);
}

// Email observer
class EmailObserver implements OrderStatusObserver {
    @Override
    public void update(String orderStatus) {
        System.out.println("[Email] Notification: Your order status changed to " + orderStatus);
    }
}

// SMS observer
class SMSObserver implements OrderStatusObserver {
    @Override
    public void update(String orderStatus) {
        System.out.println("[SMS] Notification: Your order status changed to " + orderStatus);
    }
}

// ObservableOrder extends Order and notifies observers on status change
class ObservableOrder extends ModelClasses.Order {
    private List<OrderStatusObserver> observers = new ArrayList<>();

    public ObservableOrder(ModelClasses.Customer customer) {
        super(customer);
    }

    public void addObserver(OrderStatusObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderStatusObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (OrderStatusObserver obs : observers) {
            obs.update(getStatus().toString());
        }
    }

    @Override
    public void setStatus(OrderStatus status) {
        super.setStatus(status);
        notifyObservers();
    }
}



