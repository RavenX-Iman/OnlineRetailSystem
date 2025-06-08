package onlineretailsystem;
// Shipping cost calculation strategy interface
interface ShippingStrategy {
    double calculateShippingCost(double weight);
}

// Flat rate shipping implementation
class FlatRateShipping implements ShippingStrategy {
    @Override
    public double calculateShippingCost(double weight) {
        return 10.0; // flat fee $10
    }
}

// Weight-based shipping implementation
class WeightBasedShipping implements ShippingStrategy {
    @Override
    public double calculateShippingCost(double weight) {
        return weight * 3.0; // $3 per kg
    }
}

// Shipping context that uses a strategy
class ShippingContext {
    private ShippingStrategy strategy;

    public ShippingContext(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public double getShippingCost(double weight) {
        return strategy.calculateShippingCost(weight);
    }
}



