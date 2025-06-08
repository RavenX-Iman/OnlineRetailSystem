package onlineretailsystem;
import java.math.BigDecimal;

    interface RetailFactory {
        Object create(String type);
    }

    public class SimpleRetailFactory implements RetailFactory {

        @Override
        public Object create(String type) {
            switch(type.toLowerCase()) {
                case "customer":
                    return new ModelClasses.Customer("Ali", "Khan", "ali@example.com", "03001234567",
                            "Street 1", "Karachi", "Sindh", "74400", "Pakistan");
                case "category":
                    return new ModelClasses.Category(1, "Books", "Books category");
                case "product":
                    ModelClasses.Category cat = new ModelClasses.Category(2, "Electronics", "Electronics items");
                    return new ModelClasses.Product("Smartphone", cat, new BigDecimal("15000"), 50);
                case "order":
                    ModelClasses.Customer cust = new ModelClasses.Customer("Ayesha", "Malik", "ayesha@example.com", "03111234567",
                            "Street 2", "Lahore", "Punjab", "54000", "Pakistan");
                    return new ModelClasses.Order(cust);
                case "payment":
                    ModelClasses.Order order = new ModelClasses.Order(new ModelClasses.Customer("Sara", "Ali", "sara@example.com", "03211234567",
                            "Street 3", "Islamabad", "ICT", "44000", "Pakistan"));
                    return new ModelClasses.Payment(order, new BigDecimal("2000"), ModelClasses.Payment.PaymentMethod.CASH_ON_DELIVERY);
                default:
                    return null;
            }
        }
    }





