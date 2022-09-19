public class OrderGenerator {

    public static Order orderWithValidIngredients() {
        return new Order(Ingredients.VALID);
    }

    public static Order orderWithoutIngredients() {
        return new Order(null);
    }

    public static Order orderWithInvalidIngredients() {
        return new Order(Ingredients.INVALID);
    }
}
