namespace Backend.Models.DataModels
{
    public class OrderItem
    {
        public int OrderId { get; set; }
        public int DishId { get; set; }
        public int Amount { get; set; }
    }
}
