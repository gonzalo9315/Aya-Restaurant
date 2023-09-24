using System.ComponentModel.DataAnnotations;

namespace Backend.Models.DataModels
{
    public class Dish : BaseEntity
    {
        [Required]
        public string Name { get; set; }
        [StringLength(300)]
        public string Description { get; set; }
        public string Ingredients { get; set; }
        [Required]

        public decimal Price { get; set; }
        [Required]

        public string Photo { get; set; }
    }
}
